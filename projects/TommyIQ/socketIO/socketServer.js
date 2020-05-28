const IO = (server) => {
  const io = require('socket.io').listen(server);

  const sockets = {};
  
  // waiting for connection
  io
    .of("/direct")
    .on('connection', (socket) => {
      console.log("user has connected");
      socket.on("disconnect", () => {
        console.log('user has disconnected');

        delete sockets[userId];
      });


      let userId, partnerId;


      // getting user's id
      socket.on("userId", (roomData) => {
        userId = roomData.userId;
        partnerId = roomData.partnerId;

        sockets[userId] = socket;
        // console.log(userId, partnerId);
        // console.log(sockets);
      });

      // when message is sent
      socket.on('serverMessage', (msg) => {
        
        if(sockets[partnerId] != undefined) {
          // console.log('SENT TO PARTNER');
          sockets[partnerId].emit('sendMessagePartner', msg);
        }
        // console.log('SENT TO USER');
        sockets[userId].emit('sendMessageUser', msg);

      });
  
    });
  
}

module.exports = IO;