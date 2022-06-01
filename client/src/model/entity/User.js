export class User {

  password;
  repeatPassword;

  userId;

  firstname;
  lastname;

  phoneTitle;
  phoneNumber;

  role;

  email;
  username;

  birthDate;
  accountCreationDate;
  lastLoginDate;
  lastLogoutDate;

  name = {
    firstname: "",
    lastname: ""
  };

  primaryPhone = {
    title: "",
    number: ""
  }

  favourites = [];
  searches = [];

}
