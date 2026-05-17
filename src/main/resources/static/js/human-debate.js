window.SpeechRecognition =
window.SpeechRecognition ||
window.webkitSpeechRecognition;


// =========================
// VOICE
// =========================

let recognition =
new webkitSpeechRecognition();

recognition.continuous=true;
recognition.interimResults=true;
recognition.lang="en-US";

let isListening=false;


// =========================
// URL PARAMS
// =========================

let params=
new URLSearchParams(
window.location.search
);

let roomId=
params.get(
"roomId"
);

let username=
params.get(
"username"
);

let stompClient=null;


// =========================
// CONNECT SOCKET
// =========================

function connect(){

let socket=
new SockJS("/ws");

stompClient=
Stomp.over(socket);

stompClient.connect(

{},

function(){

// CHAT

stompClient.subscribe(

"/topic/messages",

function(msg){

let data=

JSON.parse(
msg.body
);

if(
data.roomId!==roomId
)
return;

showMessage(
data
);

}
);


// PLAYER STATUS

stompClient.subscribe(

"/topic/status",

function(msg){

let data=

JSON.parse(
msg.body
);

if(
data.roomId!==roomId
)
return;

if(
data.type==="JOIN"
){

showStatus(

data.sender+
" joined"

);

checkPlayers();

}

}
);


// END EVENT

stompClient.subscribe(

"/topic/end",

function(msg){

let data=

JSON.parse(
msg.body
);

if(
data.roomId!==roomId
)
return;

loadDebateResult();

}
);


// JOIN EVENT

stompClient.send(

"/app/join",

{},

JSON.stringify({

roomId:
roomId,

sender:
username

})

);

}
);

}


// =========================
// LOAD ROOM
// =========================

function loadRoom(){

fetch(

`/human/room/${roomId}`

)

.then(
res=>res.json()
)

.then(room=>{

if(!room)
return;

document.getElementById(
"topic"
)

.innerText=
room.topic;


document.getElementById(
"roomIdText"
)

.innerText=
room.roomId;


checkPlayers();

});

}


// =========================
// CHECK PLAYERS
// =========================

function checkPlayers(){

fetch(

`/human/room/${roomId}`

)

.then(
res=>res.json()
)

.then(room=>{

if(!room)
return;

let count=

room.player2
?
2
:
1;


document.getElementById(
"playerStatus"
)

.innerText=

count+
" / 2";


if(room.player2){

document.getElementById(
"waitingBox"
)

.style.display=
"none";


document.getElementById(
"debateBox"
)

.style.display=
"block";
}

});

}


// =========================
// MESSAGE
// =========================

function showMessage(msg){

let chat=

document.getElementById(
"chatBox"
);

chat.innerHTML+=

`

<div class="message">

<b>

${msg.sender}:

</b>

${msg.content}

</div>

`;

chat.scrollTop=
chat.scrollHeight;

}



// =========================
// SEND
// =========================

function sendMessage(){

let input=

document.getElementById(
"messageInput"
);

let content=

input.value.trim();

if(content==="")
return;


stompClient.send(

"/app/chat",

{},

JSON.stringify({

roomId:
roomId,

sender:
username,

content:
content

})

);


input.value="";

}



// =========================
// STATUS
// =========================

function showStatus(text){

let chat=

document.getElementById(
"chatBox"
);

chat.innerHTML+=

`

<div class="status-msg">

${text}

</div>

`;

}



// =========================
// END DEBATE
// =========================

function endDebate(){

document.getElementById(
"endBtn"
)

.disabled=true;


fetch(

`/human/end/${roomId}`,

{
method:"POST"
}

)

.then(
res=>res.json()
)

.then(room=>{

console.log(
room
);

if(!room){

alert(
"Unable to generate result"
);

return;
}

showResult(
room
);

})

.catch(error=>{

console.log(
error
);

alert(
"Error generating result"
);

});

}



// =========================
// RESULT
// =========================

function loadDebateResult(){

fetch(

`/human/room/${roomId}`

)

.then(
res=>res.json()
)

.then(room=>{

if(
room &&
room.result
){

showResult(
room
);

}

});

}


function showResult(room){

document.getElementById(
"resultBox"
)

.style.display=
"block";


let winner=

room.winner
||
"Draw";


let result=

room.result
||
"No result generated";


document.getElementById(
"resultText"
)

.innerHTML=

`

<h3>

Winner:
${winner}

</h3>

<br>

<div class="ai-result">

${result.replace(/\n/g,"<br>")}

</div>

`;


document.getElementById(
"messageInput"
)

.disabled=true;

}



// =========================
// VOICE INPUT
// =========================

function startVoice(){

if(isListening){

recognition.stop();

isListening=false;

document.getElementById(
"voiceBtn"
)

.innerText=
"🎤 Speak";

return;
}

isListening=true;

document.getElementById(
"voiceBtn"
)

.innerText=
"⏹ Stop";

recognition.start();

}


recognition.onresult=

function(event){

let text="";

for(

let i=
event.resultIndex;

i<
event.results.length;

i++

){

text+=

event.results[i][0]
.transcript;

}

document.getElementById(
"messageInput"
)

.value=
text;

};


recognition.onend=

function(){

if(
isListening
){

recognition.start();
}

};


// =========================
// BUTTON
// =========================

document.getElementById(
"voiceBtn"
)

.addEventListener(

"click",

startVoice

);


// =========================
// INIT
// =========================

connect();

loadRoom();

setInterval(
checkPlayers,
2000
);