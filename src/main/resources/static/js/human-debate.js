let params =
    new URLSearchParams(window.location.search);

let roomId =
    params.get("roomId");

let username =
    params.get("username");

let stompClient = null;

// =========================
// CONNECT
// =========================
function connect(){

    let socket =
        new SockJS('/ws');

    stompClient =
        Stomp.over(socket);

    stompClient.connect({}, function () {

        // CHAT
        stompClient.subscribe(
            '/topic/messages',
            function (msg) {

                let data =
                    JSON.parse(msg.body);

                if(data.roomId !== roomId)
                    return;

                showMessage(data);
            }
        );

        // STATUS
        stompClient.subscribe(
            '/topic/status',
            function (msg) {

                let data =
                    JSON.parse(msg.body);

                if(data.roomId !== roomId)
                    return;

                if(data.type === "JOIN"){

                    showStatus(
                        data.sender + " joined"
                    );

                    checkPlayers();
                }
            }
        );

        // END DEBATE
        stompClient.subscribe(
            '/topic/end',
            function(msg){

                let data =
                    JSON.parse(msg.body);

                if(data.roomId !== roomId)
                    return;

                loadDebateResult();
            }
        );

        // JOIN EVENT
        stompClient.send(
            "/app/join",
            {},
            JSON.stringify({
                roomId: roomId,
                sender: username
            })
        );
    });
}

// =========================
// CHECK PLAYERS
// =========================
function checkPlayers(){

    fetch(`/human/room/${roomId}`)

    .then(res => res.json())

    .then(room => {

        if(!room){
            return;
        }

        let count =
            room.player2 ? 2 : 1;

        document.getElementById(
            "playerStatus"
        ).innerText =
            count + " / 2";

        // START
        if(room.player2){

            document.getElementById(
                "waitingBox"
            ).style.display = "none";

            document.getElementById(
                "debateBox"
            ).style.display = "block";
        }
    });
}

// =========================
// LOAD ROOM
// =========================
function loadRoom(){

    fetch(`/human/room/${roomId}`)

    .then(res => res.json())

    .then(room => {

        if(room){

            document.getElementById(
                "topic"
            ).innerText =
                room.topic;

            document.getElementById(
                "roomIdText"
            ).innerText =
                room.roomId;

            checkPlayers();
        }
    });
}

// =========================
// SHOW MESSAGE
// =========================
function showMessage(msg){

    let chatBox =
        document.getElementById(
            "chatBox"
        );

    chatBox.innerHTML += `
        <div class="message">
            <b>${msg.sender}:</b>
            ${msg.content}
        </div>
    `;

    chatBox.scrollTop =
        chatBox.scrollHeight;
}

// =========================
// SEND MESSAGE
// =========================
function sendMessage(){

    let input =
        document.getElementById(
            "messageInput"
        );

    let content =
        input.value.trim();

    if(content === "")
        return;

    stompClient.send(
        "/app/chat",
        {},
        JSON.stringify({
            roomId: roomId,
            sender: username,
            content: content
        })
    );

    input.value = "";
}

// =========================
// STATUS
// =========================
function showStatus(text){

    let chatBox =
        document.getElementById(
            "chatBox"
        );

    if(chatBox){

        chatBox.innerHTML += `
            <div class="status-msg">
                ${text}
            </div>
        `;
    }
}

// =========================
// END DEBATE
// =========================
function endDebate(){

    document.getElementById(
        "endBtn"
    ).disabled = true;

    fetch(`/human/end/${roomId}`, {
        method: "POST"
    })

    .then(res => res.json())

    .then(room => {

        if(!room){
            alert("Error ending debate");
            return;
        }

        // SOCKET EVENT
        stompClient.send(
            "/app/end",
            {},
            JSON.stringify({
                roomId: roomId
            })
        );

        showResult(room);
    });
}

// =========================
// LOAD RESULT
// =========================
function loadDebateResult(){

    fetch(`/human/room/${roomId}`)

    .then(res => res.json())

    .then(room => {

        if(room && room.result){

            showResult(room);
        }
    });
}

// =========================
// SHOW RESULT
// =========================
function showResult(room){

    // RESULT BOX
    document.getElementById(
        "resultBox"
    ).style.display = "block";

    // WINNER
    document.getElementById(
        "winnerText"
    ).innerText =
        room.winner || "AI Decision";

    // RESULT
    document.getElementById(
        "resultText"
    ).innerHTML =
        `
        <div class="ai-result">
            ${room.result.replace(/\n/g, "<br>")}
        </div>
        `;

    // LOCK CHAT
    document.getElementById(
        "messageInput"
    ).disabled = true;
}

// =========================
// INIT
// =========================
connect();

loadRoom();

setInterval(checkPlayers, 2000);