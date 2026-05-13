let currentUsername = "";

async function searchUser() {

    currentUsername =
        document.getElementById("username").value;

    const response =
        await fetch(`/auth/get-question?username=${currentUsername}`);

    const data =
        await response.json();

    if (data.success) {

        document.getElementById("question").style.display = "block";
        document.getElementById("answer").style.display = "block";
        document.getElementById("verifyBtn").style.display = "block";

        document.getElementById("question").value =
            data.question;

    } else {

        alert(data.error);
    }
}

async function verifyAnswer() {

    const answer =
        document.getElementById("answer").value;

    const response =
        await fetch("/auth/verify-answer", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({

                username: currentUsername,
                answer: answer
            })
        });

    const data =
        await response.json();

    if (data.success) {

        alert("Answer verified");

        document.getElementById("resetForm").style.display =
            "block";

    } else {

        alert(data.error);
    }
}

document.getElementById("resetForm")
    .addEventListener("submit", async function (e) {

        e.preventDefault();

        const newPassword =
            document.getElementById("newPassword").value;

        const confirmPassword =
            document.getElementById("confirmPassword").value;

        if (newPassword !== confirmPassword) {

            alert("Passwords do not match");
            return;
        }

        const response =
            await fetch("/auth/reset-password", {

                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({

                    username: currentUsername,
                    newPassword: newPassword
                })
            });

        const data =
            await response.json();

        if (data.success) {

            alert("Password reset successful");

            window.location.href = "/login";

        } else {

            alert(data.error);
        }
    });