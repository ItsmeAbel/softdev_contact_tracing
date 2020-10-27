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


from datetime import datetime
class Interaction(models.Model):
    user1 = models.ForeignKey(User, related_name="user1", on_delete=models.CASCADE)
    user2 = models.ForeignKey(User, related_name="user2", on_delete=models.CASCADE)
    date = models.DateField(null=True)

    def set_date(self, date=None):
        """
        Sets date, if date specified expects format %Y-%m-%d
        """
        if date == None:
            date = datetime.now()
        else:
            date = datetime.strptime(date, "%Y-%m-%d")
        self.date = date
        self.save()
    
    def __str__(self):
        return "{} and {}".format(self.user1.__str__(),self.user2.__str__())

MAX_UUIDS_LEN = 500
from django.contrib.auth import get_user_model
User = get_user_model()

class Profile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    identifier = models.TextField(max_length=MAX_UUIDS_LEN, blank=True)
    
    infected = models.BooleanField(default=False)
    infection_date = models.DateField(null=True, blank=True)
    contact = models.BooleanField(default=False)
    contact_date = models.DateField(null=True, blank=True)
    
    unconfirmed_infected = models.BooleanField(default=False)
    unconfirmed_infection_date = models.DateField(null=True, blank=True)
    unconfirmed_contact = models.BooleanField(default=False)
    unconfirmed_contact_date = models.DateField(null=True, blank=True)

    def __str__(self):
        return self.user.__str__()
    
    def maintenance(self):
        """
        Checks dates so that an infection expires after the correct time.
        """
        expiration_time = 20 # 20 days for an infection to expire
        
        if (self.infection_date - datetime.now()).days >= expiration_time:
            self.infected = False
            self.infection_date = None
            self.save()
        if (self.unconfirmed_infection_date - datetime.now()).days >= expiration_time:
            self.unconfirmed_contact = False
            self.unconfirmed_infection_date = None
            self.save()
    
    def set_infected(self, date=None):
        """
        Sets Profile as being infected.
        """
        if date == None:
            date = datetime.now()
        else:
            date = datetime.strptime(date, "%Y-%m-%d")
        self.infected = True
        self.infection_date = date
        self.save()

    def set_contact(self, date=None):
        """
        Sets Profile as having had contact with an infected user
        Sets contact date as current date if not specified
        """
        if date == None:
            date = datetime.now()
        else:
            date = datetime.strptime(date, "%Y-%m-%d")
        self.contact = True
        self.contact_date = date
        self.save()

    def infection_check(self):
        """
        Goes through all interactions and checks whether other have
        interacted with infected user. Sets the users as having had
        contact.
        """
        if not self.infected: return
        
        spread_vector = Interaction.objects.filter(user1=self.user)
        for contaminated_interaction in spread_vector:
            contact_user = contaminated_interaction.user2
            contact_user_profile = Profile.objects.filter(user=contact_user)[0]
            contact_user_profile.set_contact()

        spread_vector = Interaction.objects.filter(user2=self.user)
        for contaminated_interaction in spread_vector:
            contact_user = contaminated_interaction.user1
            contact_user_profile = Profile.objects.filter(user=contact_user)[0]
            contact_user_profile.set_contact()

    def set_unconfirmed_contact(self, date=None):
        """
        Sets Profile as having had unconfirmed_contact with an unconfirmed_contact user
        Sets unconfirmed_contact date as current date if not specified
        """
        if date == None:
            date = datetime.now()
        else:
            date = datetime.strptime(date, "%Y-%m-%d")
        self.unconfirmed_contact = True
        self.unconfirmed_contact_date = date
        self.save()
    
    def set_unconfirmed_infected(self, date=None):
        """
        Sets Profile as being unconfirmed_infected.
        """
        if date == None:
            date = datetime.now()
        else:
            date = datetime.strptime(date, "%Y-%m-%d")
        self.unconfirmed_infected = True
        self.unconfirmed_infection_date = date
        self.save()

    def unconfirmed_infection_check(self):
        """
        Goes through all interactions and checks whether other have
        interacted with unconfirmed_contact user. Sets the users as having had
        unconfirmed_contact.
        """
        if not self.unconfirmed_infected: return
        
        spread_vector = Interaction.objects.filter(user1=self.user)
        for contaminated_interaction in spread_vector:
            unconfirmed_contact_user = contaminated_interaction.user2
            unconfirmed_contact_user_profile = Profile.objects.filter(user=unconfirmed_contact_user)[0]
            unconfirmed_contact_user_profile.set_unconfirmed_contact()

        spread_vector = Interaction.objects.filter(user2=self.user)
        for contaminated_interaction in spread_vector:
            unconfirmed_contact_user = contaminated_interaction.user1
            unconfirmed_contact_user_profile = Profile.objects.filter(user=unconfirmed_contact_user)[0]
            unconfirmed_contact_user_profile.set_unconfirmed_contact()

    def statistics(self):
        # returns number of confirmed and unconfirmed contacts with infected
        count_confirmed = 0
        count_unconfirmed = 0
        count_interactions = 0  

        spread_vector = Interaction.objects.filter(user1=self.user)
        count_interactions += len(spread_vector)
        for contaminated_interaction in spread_vector:
            contact_user = contaminated_interaction.user2
            profi = Profile.objects.filter(user=contact_user)[0]
            if profi.infected: count_confirmed += 1
            if profi.unconfirmed_infected: count_unconfirmed += 1

        spread_vector = Interaction.objects.filter(user2=self.user)
        count_interactions += len(spread_vector)
        for contaminated_interaction in spread_vector:
            contact_user = contaminated_interaction.user1
            profi = Profile.objects.filter(user=contact_user)[0]
            if profi.infected: count_confirmed += 1
            if profi.unconfirmed_infected: count_unconfirmed += 1

        return count_confirmed, count_unconfirmed, count_interactions
             














