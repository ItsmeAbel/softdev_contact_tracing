
from django.contrib.auth.models import AbstractUser, BaseUserManager
from django.db import models
from django.utils.translation import ugettext_lazy as _


class UserManager(BaseUserManager):
    """Define a model manager for User model with no username field."""

    use_in_migrations = True

    def _create_user(self, email, password, **extra_fields):
        """Create and save a User with the given email and password."""
        if not email:
            raise ValueError('The given email must be set')
        email = self.normalize_email(email)
        user = self.model(email=email, **extra_fields)
        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_user(self, email, password=None, **extra_fields):
        """Create and save a regular User with the given email and password."""
        extra_fields.setdefault('is_staff', False)
        extra_fields.setdefault('is_superuser', False)
        return self._create_user(email, password, **extra_fields)

    def create_superuser(self, email, password, **extra_fields):
        """Create and save a SuperUser with the given email and password."""
        extra_fields.setdefault('is_staff', True)
        extra_fields.setdefault('is_superuser', True)

        if extra_fields.get('is_staff') is not True:
            raise ValueError('Superuser must have is_staff=True.')
        if extra_fields.get('is_superuser') is not True:
            raise ValueError('Superuser must have is_superuser=True.')

        return self._create_user(email, password, **extra_fields)


class User(AbstractUser):
    """User model."""

    username = None
    email = models.EmailField(_('email address'), unique=True)

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = []

    objects = UserManager()



class Interaction(models.Model):
    user1 = models.ForeignKey(User, related_name="user1", on_delete=models.CASCADE)
    user2 = models.ForeignKey(User, related_name="user2", on_delete=models.CASCADE)
    date = models.DateField()
    
    def __str__(self):
        return "{} and {}".format(self.user1.__str__(),self.user2.__str__())

MAX_UUIDS_LEN = 500
from django.contrib.auth import get_user_model
User = get_user_model()
class Profile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    uuids = models.TextField(max_length=MAX_UUIDS_LEN, blank=True)
    infected = models.BooleanField(default=False)
    infection_date = models.DateField(null=True, blank=True)

    def __str__(self):
        return self.user.__str__()

    def infection_check(self):
        """
        Goes through all interactions and checks whether other have
        interrected with infected user.
        """
        # TODO should only first degree contact users be set as infected
        # or should every possible node in the graph be traversed?
        if not self.infected: return
        
        spread_vector = Interaction.objects.filter(user1=self.user)
        for contaminated_interaction in spread_vector:
            infected_user = contaminated_interaction.user2
            infected_user.infected = True
        spread_vector = Interaction.objects.filter(user2=self.user)
        for contaminated_interaction in spread_vector:
            infected_user = contaminated_interaction.user1
            infected_user.infected = True














