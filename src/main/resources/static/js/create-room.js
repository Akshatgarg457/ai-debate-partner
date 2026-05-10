// =========================
// GLOBALS
// =========================
let currentRoomId = "";
let currentUsername = "";

// =========================
// CREATE ROOM
// =========================
function createRoom(){

    let username =
        document.getElementById("username").value;

    let topic =
        document.getElementById("topic").value;

    if(topic.trim() === ""){
        alert("Enter topic");
        return;
    }

    fetch(
        `/human/create?topic=${encodeURIComponent(topic)}&username=${encodeURIComponent(username)}`,
        {
            method: "POST"
        }
    )
    .then(res => res.json())
    .then(data => {

        currentRoomId = data.roomId;
        currentUsername = username;

        document.getElementById("resultBox")
            .style.display = "block";

        document.getElementById("roomIdDisplay")
            .value = data.roomId;
    });
}

// =========================
// COPY ROOM ID
// =========================
function copyRoomId(){

    let input =
        document.getElementById("roomIdDisplay");

    input.select();

    document.execCommand("copy");

    alert("Room ID copied!");
}

// =========================
// ENTER CREATED ROOM
// =========================
function enterRoom(){

    window.location.href =
        `/human-debate?roomId=${currentRoomId}&username=${currentUsername}`;
}

// =========================
// JOIN ROOM
// =========================
function joinRoom(){

    let username =
        document.getElementById("joinUsername").value;

    let roomId =
        document.getElementById("joinRoomId").value;

    if(roomId.trim() === ""){
        alert("Enter Room ID");
        return;
    }

    fetch(
        `/human/join?roomId=${roomId}&username=${username}`,
        {
            method: "POST"
        }
    )
    .then(res => res.json())
    .then(data => {

        if(!data){
            alert("Room not found!");
            return;
        }

        window.location.href =
            `/human-debate?roomId=${roomId}&username=${username}`;
    });
}

// =========================
// SHOW CREATE TAB
// =========================
function showCreateTab(){

    document.getElementById("createSection")
        .style.display = "block";

    document.getElementById("joinSection")
        .style.display = "none";

    document.getElementById("createTabBtn")
        .classList.add("active");

    document.getElementById("joinTabBtn")
        .classList.remove("active");
}

// =========================
// SHOW JOIN TAB
// =========================
function showJoinTab(){

    document.getElementById("createSection")
        .style.display = "none";

    document.getElementById("joinSection")
        .style.display = "block";

    document.getElementById("joinTabBtn")
        .classList.add("active");

    document.getElementById("createTabBtn")
        .classList.remove("active");
}