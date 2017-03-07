/**
 * SOSController
 *
 * @description :: Server-side logic for managing SOS
 * @help        :: See http://sailsjs.org/#!/documentation/concepts/Controllers
 */
var request = require('request');
var t1,u1,ds;
var t2,u2,du;
var valuessos;
var valuesuser;
var valuesdsos, valuesduser,length;
module.exports = {

  'new': function (req, res) {
    res.view();
  },

  create : function(req, res, next) {
    console.log("Entered into user controller");
    SOS.create(req.params.all(), function sosCreated(err, sos) {
      // if (err) {
      //   console.log("This is the error");
      //   console.log(err);
      //
      //   req.session.flash = {
      //     err: err
      //   };
      // }
      req.session.authenticated = true;

      if (sos) {
        var thankyou = [{
          name: 'usernamePasswordRequired',
          message: 'Thankyou.'
        }];

        console.log('Thankyou');



        User.find(function foundUsers(err, users) {
                  if (err) return next(err);
                  //console.log(users);
                  //res.status(200).json(users);


                  users.forEach(function (user) {
                    if(!user){
                      res.status(200).json('Not done!')
                    }
                    //console.log(user);
                    console.log("inside users foreach");
                    valuessos = (sos.time).split(":");
                    valuesuser = (user.time).split(":");
                    valuesdsos = (sos.date).split("-");
                    valuesduser = (user.date).split("-");

                    console.log("Here is my code of calculating");

                    t1 = parseFloat(valuessos[0]);
                              t2 = parseFloat(valuessos[1]);
                              u1 = parseFloat(valuesuser[0]);
                              u2 = parseFloat(valuesuser[1]);
                              ds = parseFloat(valuesdsos[2]);
                              du = parseFloat(valuesduser[2]);
                              console.log("u2-15 is ", u2 - 15);
                              console.log("t2 is ", t2);
                              console.log("u2+15 is ", u2 + 15);
                    Mailer.sendWelcomeMail(user);
                    return;


                    if (du == ds) {
                                if (u2 - 10 < t2 && t2 < u2 + 10) {
                                  console.log("t2 is greater than u2-15");
                                  if (user.longitude === sos.longitude) {
                                    console.log("help needed");
                                    Mailer.sendWelcomeMail(user);
                                     res.status(200).json(user);
                                    // return;
                                  }
                                }

                              }
                              else {
                                Mailer.sendWelcomeMail(user);
                                console.log("No help needed");
                              }


                  });
        });

        //Mailer.sendWelcomeMail(user);

      }


// res.json(200, {
//   user:  user
// });

    });
  },




  match: function(req, res, next) {

    SOS.findOne(req.param('id'), function foundSOS(err, sos) {
      if (err) return next(err);
      if (!sos) return next();

      User.find(function foundUsers(err, users){
        if(err) return next(err);
        console.log(users);
        //res.status(200).json(users);

        users.forEach(function(user) {
          console.log(user);
          console.log("worked");
          if (user.longitude === sos.longitude) {
            console.log("inside if");
            Mailer.sendWelcomeMail(user);
          }
        });
        res.status(200).json(users);


      });


      //res.status(200).json(sos);
    });

    // User.findOrCreate(users).exec(function(err, users){
    //   console.log(users);
    // });



  },



  index : function(req, res, next){

    SOS.find(function foundSOSs(err, soss){
      if(err) return next(err);
      res.status(200).json(soss);

    });
  },





};

