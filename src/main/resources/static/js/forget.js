let currentUsername = "";

async function searchUser() {

    currentUsername =
        document.getElementById("username").value;

    const response =
        await fetch(`/auth/question?username=${currentUsername}`);

    const data =
        await response.json();

    if(data.success){

        document.getElementById(
            "question"
        ).value = data.question;

    } else {

        alert(data.error);
    }
}

async function verifyAnswer() {

    const answer =
        document.getElementById("answer").value;

    const response =
        await fetch("/auth/verify", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({

            username: currentUsername,
            answer
        })
    });

    const data =
        await response.json();

    if(data.success){

        alert("Answer verified");

    } else {

        alert(data.error);
    }
}

async function resetPassword() {

    const password =
        document.getElementById("password").value;

    const confirmPassword =
        document.getElementById("confirmPassword").value;

    if(password !== confirmPassword){

        alert("Passwords do not match");
        return;
    }

    const response =
        await fetch("/auth/reset", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({

            username: currentUsername,
            password
        })
    });

    const data =
        await response.json();

    if(data.success){

        alert("Password reset successful");

        window.location.href =
            "/login";

    } else {

        alert(data.error);
    }
}