const questions = {

    anime:
        "Your School Name",

    superhero:
        "Your Favorite Superhero",

    cricketer:
        "Your Favorite Cricketer",

    lucky:
        "Your Lucky Number",

    nickname:
        "Your Childhood Nickname"
};

let currentUser = "";
let currentQuestion = "";

// ================= SEARCH USER =================

async function searchUser() {

    const user =
        document.getElementById(
            "username"
        ).value;

    try {

        const response =
            await fetch(
                "/auth/get-question?username=" + user
            );

        const result =
            await response.json();

        if (!result.success) {

            alert(result.error);

            return;
        }

        currentUser = user;

        currentQuestion =
            result.question;

        document.getElementById(
            "question"
        ).style.display = "block";

        document.getElementById(
            "answer"
        ).style.display = "block";

        document.getElementById(
            "verifyBtn"
        ).style.display = "block";

        document.getElementById(
            "question"
        ).value =
            questions[result.question];

    } catch (error) {

        alert("Server error");
    }
}

// ================= VERIFY ANSWER =================

async function verifyAnswer() {

    const answer =
        document.getElementById(
            "answer"
        ).value;

    try {

        const response =
            await fetch(
                "/auth/verify-answer",
                {

                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body: JSON.stringify({

                        username:
                            currentUser,

                        question:
                            currentQuestion,

                        answer:
                            answer
                    })
                });

        const result =
            await response.json();

        if (result.success) {

            document.getElementById(
                "resetForm"
            ).style.display = "block";

            document.getElementById(
                "hiddenUsername"
            ).value = currentUser;

        } else {

            alert(result.error);
        }

    } catch (error) {

        alert("Server error");
    }
}

// ================= RESET PASSWORD =================

document
    .getElementById("resetForm")

    .addEventListener(
        "submit",
        async function (e) {

            e.preventDefault();

            const username =
                document.getElementById(
                    "hiddenUsername"
                ).value;

            const newPassword =
                document.getElementById(
                    "newPassword"
                ).value;

            const confirmPassword =
                document.getElementById(
                    "confirmPassword"
                ).value;

            if (
                newPassword !==
                confirmPassword
            ) {

                alert(
                    "Passwords do not match"
                );

                return;
            }

            try {

                const response =
                    await fetch(
                        "/auth/reset-password",
                        {

                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body: JSON.stringify({

                                username,

                                newPassword
                            })
                        });

                const result =
                    await response.json();

                if (result.success) {

                    alert(
                        "Password reset successful"
                    );

                    window.location.href =
                        "/login";

                } else {

                    alert(result.error);
                }

            } catch (error) {

                alert("Server error");
            }
        });