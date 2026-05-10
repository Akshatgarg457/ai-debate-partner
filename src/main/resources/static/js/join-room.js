function joinRoom(){

    let username = document.getElementById("username").value;
    let roomId = document.getElementById("roomId").value;

    if(username === "" || roomId === ""){
        alert("Enter all fields");
        return;
    }

    fetch(`/human/join?roomId=${roomId}&username=${username}`, {
        method: "POST"
    })
    .then(res => res.json())
    .then(data => {

        if(!data){
            alert("Room not found!");
            return;
        }

        window.location.href = `/human-debate?roomId=${roomId}&username=${username}`;
    });
}