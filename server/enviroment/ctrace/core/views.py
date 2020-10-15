from django.shortcuts import render

# Create your views here.
from .models import Profile, Interaction
from .serializers import UserSerializer
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import AllowAny, IsAuthenticated
from django.contrib.auth import get_user_model
User = get_user_model()

class UserRecordView(APIView):
    """
    API View to create or get a list of all the registered
    users. GET request returns the registered users whereas
    a POST request allows to create a new user.
    """
    permission_classes = [AllowAny]

    def post(self, request):
        serializer = UserSerializer(data=request.data)
        email = request.data['email']
        query = User.objects.filter(email=email)
        print(query)
        if serializer.is_valid() and len(query) == 0:
            serializer.create(validated_data=request.data)
            return Response(
                serializer.data,
                status=status.HTTP_201_CREATED
            )
        return Response(
            {
                "error": True,
                "error_msg": serializer.error_messages,
            },
            status=status.HTTP_400_BAD_REQUEST
        )

from datetime import datetime
class StatusView(APIView):
    """
    API View to get or update current status of a user.
    GET request returns the status of a user and if there is an emergency.
    POST request updates whether user is infected, if there is an emergency
    as well as loading new interactions into the database
    """
    permission_classes = [IsAuthenticated]

    def get(self, request):
        user = request.user
        profile = Profile.objects.filter(user=user)[0]
        return Response(
                {
                    "contact": profile.contact,
                    },
            status=status.HTTP_200_OK
        )
    def post(self, request):
        user = request.user
        profile = Profile.objects.filter(user=user)[0]
        data = request.data
        keys = list(data)
        print("data\n"+str(data))
        print("keys\n"+str(keys))
        if keys == []:
            return Response(status=status.HTTP_400_BAD_REQUEST)
        if 'set_identifier' in keys:
            profile.identifier = data['set_identifier']
            profile.save()
        if 'infected' in keys and data['infected'] == 'True':
            # set user as infected
            # user can only set themselves as positive
            profile.set_infected()
            # go through all interactions and check for contact with infected
            profile.infection_check()
        if 'interactions' in keys:
            # create or update interactions in database

            for contact in data['interactions']:
                # find user by uuid
                query = Profile.objects.filter(identifier=contact["identifier"])
                # todo check for earlier interactions instead of creating new ones
                if len(query) > 1:
                    print("WTF")
                    continue
                if len(query) == 0:
                    continue
                user1 = user
                profile1 = profile
                user2 = query[0].user
                profile2 = Profile.objects.filter(user=user2)[0]
                # first interaction between users
                if profile2.infected:
                    profile1.set_contact()
                if profile1.infected:
                    profile2.set_contact()
                
                # look for earlier interaction between users
                inter_query1 = Interaction.objects.filter(user1=user1, user2=user2)
                inter_query2 = Interaction.objects.filter(user1=user2, user2=user1)
                if len(inter_query1) == 1:
                    inter = inter_query1[0]
                    inter.set_date(date=contact['date'])
                elif len(inter_query2) == 1:
                    inter = inter_query2[0]
                    inter.set_date(date=contact['date'])
                else: 
                    # first time interacting, create new entry
                    date = contact['date']
                    Interaction.objects.create(user1=user, user2=user2, date=date)
                    

        return Response(status=status.HTTP_202_ACCEPTED)

