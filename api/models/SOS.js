module.exports = {

  schema: true,

  attributes: {

    email:{
      type:'string',
      required:false,
      unique : false
    },


    latitude:{
      type:'float',
      required:false
    },

    longitude : {
      type: 'float',
      required : false
    },

    time : {
      type : 'string',
      required : false
    },

    date  : {
      type : 'string',
      required : false
    },

    toJSON: function() {
      var obj = this.toObject();
      delete obj._csrf;
      return obj;
    }
  },

  // beforeValidate: function(values, cb){
  //   console.log('Entered into before');
  //
  //   SOS.find({ where: {
  //     longitude : values.longitude
  //   }
  //   }).exec(function foundSOSs( err, soss){
  //
  //     if(soss){
  //       cb(soss);
  //     }
  //
  //     else {
  //       next('Does Not Exists!');
  //     }
  //   });
  //
  // }
};
