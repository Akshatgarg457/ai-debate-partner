window.SpeechRecognition =
window.SpeechRecognition ||
window.webkitSpeechRecognition;

const recognition =
new webkitSpeechRecognition();

recognition.continuous = false;
recognition.lang = "en-US";

const topic =
    "Social media is harmful";

function startVoice(){

    recognition.start();
}

recognition.onresult = function(event){

    const speech =
        event.results[0][0].transcript;

    document.getElementById("input").value =
        speech;

    sendMessage();
};

function sendMessage(){

    let input =
        document.getElementById("input").value;

    if(!input){
        return;
    }

    let chat =
        document.getElementById("chat");

    // USER MESSAGE

    chat.innerHTML += `
        <div class="user">
            <div class="bubble-user">
                ${input}
            </div>
        </div>
    `;

    chat.scrollTop =
        chat.scrollHeight;

    fetch("/api/debate/reply", {

        method: "POST",

        headers: {
            "Content-Type":
                "application/json"
        },

        body: JSON.stringify({

            topic: topic,
            argument: input
        })
    })

    .then(res => res.text())

    .then(data => {

        chat.innerHTML += `
            <div class="ai">
                <div class="bubble-ai">
                    ${data}
                </div>
            </div>
        `;

        // AI VOICE RESPONSE

        const speech =
            new SpeechSynthesisUtterance(data);

        speech.lang = "en-US";

        speech.rate = 1;

        window.speechSynthesis.speak(speech);

        chat.scrollTop =
            chat.scrollHeight;
    })

    .catch(error => {

        console.log(error);

        chat.innerHTML += `
            <div class="ai">
                <div class="bubble-ai">
                    AI service error.
                </div>
            </div>
        `;
    });

    document.getElementById("input").value = "";
}