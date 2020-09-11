from rest_framework import serializers
from rest_framework.validators import UniqueTogetherValidator
from django.contrib.auth import get_user_model
User = get_user_model()

class UserSerializer(serializers.ModelSerializer):

    def create(self, validated_data):
        print("DATA")
        print(validated_data)
        #user = User.objects.create_user(**validated_data)
        user = User.objects.create_user(email=validated_data['email'], password=validated_data['password'])
        return user

    class Meta:
        model = User
        fields = (
            'username',
            'password',
        )

