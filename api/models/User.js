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

    date : {
      type : 'string',
      required : false
    },



    toJSON: function() {
      var obj = this.toObject();
      delete obj._csrf;
      return obj;
    }
  },



};
