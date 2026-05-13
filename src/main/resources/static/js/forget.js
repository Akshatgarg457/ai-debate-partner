let currentUsername = "";

async function searchUser() {

    currentUsername =
        document.getElementById("username").value;

    if (!currentUsername) {

        alert("Please enter username");
        return;
    }

    try {

        const response =
            await fetch(`/auth/get-question?username=${currentUsername}`);

        const data =
            await response.json();

        console.log(data);

        if (data.success) {

            const questionField =
                document.getElementById("question");

            questionField.style.display = "block";

            questionField.value =
                data.question;

            document.getElementById("answer").style.display =
                "block";

            document.getElementById("verifyBtn").style.display =
                "block";

        } else {

            alert(data.error);
        }

    } catch (error) {

        console.error(error);

        alert("Server error");
    }
}

async function verifyAnswer() {

    const answer =
        document.getElementById("answer").value;

    if (!answer) {

        alert("Please enter answer");
        return;
    }

    try {

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

    } catch (error) {

        console.error(error);

        alert("Server error");
    }
}

document.getElementById("resetForm")
    .addEventListener("submit", async function (e) {

        e.preventDefault();

        const newPassword =
            document.getElementById("newPassword").value;

        const confirmPassword =
            document.getElementById("confirmPassword").value;

        if (!newPassword || !confirmPassword) {

            alert("Please fill all fields");
            return;
        }

        if (newPassword !== confirmPassword) {

            alert("Passwords do not match");
            return;
        }

        try {

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

                window.location.href =
                    "/login";

            } else {

                alert(data.error);
            }

        } catch (error) {

            console.error(error);

            alert("Server error");
        }
    });