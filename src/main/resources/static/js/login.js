document
    .getElementById("loginForm")
    .addEventListener("submit", async (e) => {

        e.preventDefault();

        const username =
            document.getElementById("username").value;

        const password =
            document.getElementById("password").value;

        try {

            const response =
                await fetch("/auth/login", {

                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body: JSON.stringify({
                        username,
                        password
                    })
                });

            const result =
                await response.json();

            if (result.success) {

                localStorage.setItem(
                    "username",
                    username
                );

                alert("Login successful");

                window.location.href =
                    "/dashboard";

            } else {

                alert(result.error);
            }

        } catch (error) {

            alert("Server error");
        }
    });