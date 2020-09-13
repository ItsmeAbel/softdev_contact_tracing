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
        if serializer.is_valid():
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
        print("HERE")
        user = request.user
        profile = Profile.objects.filter(user=user)[0]
        return Response(
                {
                    "infected": profile.infected,
                    },
            status=status.HTTP_200_OK
        )
    def post(self, request):
        user = request.user
        profile = Profile.objects.filter(user=user)[0]
        data = request.data
        keys = list(data)
        if keys == []:
            return Response(status=status.HTTP_400_BAD_REQUEST)
        if 'set_uuid' in keys:
            profile.uuids = data['set_uuid']
            profile.save()
        if 'interactions' in keys:
            # create or update interactions in database

            for contact in data['interactions']:
                # find user by uuid
                query = Profile.objects.filter(uuids=contact["uuid"])
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
                    profile1.infected = True
                    profile1.save()
                if profile1.infected:
                    profile2.infected = True
                    profile2.save()
                
                # look for earlier interaction between users
                print("QUERYS") 
                inter_query1 = Interaction.objects.filter(user1=user1, user2=user2)
                inter_query2 = Interaction.objects.filter(user1=user2, user2=user1)
                print(inter_query1)
                print(inter_query2)
                if len(inter_query1) == 1:
                    inter = inter_query1[0]
                    inter.date = contact['date']
                    inter.save()
                elif len(inter_query2) == 1:
                    inter = inter_query2[0]
                    inter.date = contact['date']
                    inter.save()
                else: 
                    # first time interacting, create new entry
                    date = contact['date']
                    Interaction.objects.create(user1=user, user2=user2, date=date)
                    
        if 'infected' in keys and data['infected'] == 'True':
            # set user as infected
            # user can only set themselves as positive
            profile.infected = True
            profile.infection_date = str(datetime.now()).split(' ')[0]
            profile.save()

            # go through all interactions and check for contact with infected
            profile.infection_check()

        return Response(status=status.HTTP_202_ACCEPTED)

