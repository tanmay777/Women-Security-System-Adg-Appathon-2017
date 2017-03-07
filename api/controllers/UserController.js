/**
 * UserController
 *
 * @description :: Server-side logic for managing users
 * @help        :: See http://sailsjs.org/#!/documentation/concepts/Controllers
 */

module.exports = {

  create : function(req, res, next) {

    console.log("Entered into user controller");

    User.create(req.params.all(), function userCreated(err, user) {
      if (err) {
        console.log("This is the error");
        console.log(err);

        req.session.flash = {
          err: err
        };
        res.status(200).json(err);
        return;
      }

      req.session.authenticated = true;
      req.session.User = user;


      if (user) {
        //Mailer.sendWelcomeMail(user);
        res.status(200).json(user);
        return;
      }


// res.json(200, {
//   user:  user
// });

    });
  },

  index : function(req, res, next){



    User.find(function foundUsers(err, users){
      if(err) return next(err);

      for(user in users){
        console.log(user);

      }

      if(user){

      }
      res.status(200).json(users);
    });

  },


  };

