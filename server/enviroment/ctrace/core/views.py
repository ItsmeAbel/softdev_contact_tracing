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
        # get profile
        profile = Profile.objects.filter(user=user)[0]
        print(profile)

        return Response(
                {
                    "infected": profile.infected,
                    },
            status=status.HTTP_200_OK
        )
    def post(self, request):
        user = request.user

        
        # check that data is correct



