const processMessage = (msg, output, name, partnerId, mode) => {
    let html = `
      <div class="element-msg">
        <div class="message">
          <p class="message__parag">${msg}</p>
        </div>
      </div> 
    `;

    if(mode === "USER") {
      html = `
      <div class="element-msg">
        <div class="message message__user">
          <p class="message__parag">${msg}</p>
        </div>
      </div> 
    `;
    }

  output.insertAdjacentHTML('beforeEnd', html);

  if(mode === 'USER') {
    axios.post('/direct/save', {
      message: msg,
      name: name,
      DMid: partnerId
    })
      .then((response) =>  console.log(response) )
      .catch((err) =>  console.log(err) );
  }

}


const socketConnection = (formSel, MsgContainerSel) => {
  const socket = io.connect("/direct");

  // getting user's id
  const userId = document.querySelector('#user-id').getAttribute('data-userId');
  const partnerId = document.querySelector('#DMuser-name').getAttribute('data-id');
  // DMuser name
  const name = document.querySelector('#DMuser-name').getAttribute('data-name');
  
  // making data for room
  const roomData = {
    userId,
    partnerId
  };


  // sending ids of both users
  socket.emit('userId', roomData);

  
  // -- recieving msg from server 
  // if user then save to DB the msg 
  socket.on('sendMessageUser', (msg) => {
    processMessage(msg, output, name, partnerId, 'USER');
  });
  
  // if partner then don't save to DB the msg
  socket.on('sendMessagePartner', (msg) => {
    processMessage(msg, output, name, partnerId, 'PARTNER');
  });


  // posting message to server side
  const form = document.querySelector(formSel);
  const output = document.querySelector(MsgContainerSel);

  form.addEventListener('submit', (e) => {
    e.preventDefault();

    const msg = document.querySelector('.textarea').value;
    document.querySelector('.textarea').value = '';

    if (msg.trim() !== '') {
      socket.emit('serverMessage', msg);
    } 
    else return;


  });

};

socketConnection('.form', '.insertMessages');