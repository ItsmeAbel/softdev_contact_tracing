from rest_framework import serializers
from rest_framework.validators import UniqueTogetherValidator
from django.contrib.auth import get_user_model
from .models import Profile
import uuid
User = get_user_model()

class UserSerializer(serializers.ModelSerializer):

    def create(self, validated_data):
        print("DATA")
        print(validated_data)
        #user = User.objects.create_user(**validated_data)
        user = User.objects.create_user(email=validated_data['email'], password=validated_data['password'])
        # remove x amount of characters due to how BT works
        cut = 18
        ident =  str(uuid.uuid1())[::-1][cut:]
        profile = Profile.objects.create(user=user, identifier=ident)
        return user

    class Meta:
        model = User
        fields = (
            'username',
            'password',
        )

