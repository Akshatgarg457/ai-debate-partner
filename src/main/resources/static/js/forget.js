document
    .getElementById("forgetForm")
    .addEventListener("submit", async (e) => {

        e.preventDefault();

        const username =
            document.getElementById("username").value;

        const newPassword =
            document.getElementById("newPassword").value;

        const response =
            await fetch("/auth/reset-password", {

                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    username,
                    newPassword
                })
            });

        const result = await response.json();

        if (result.success) {

            alert("Password updated");

            window.location.href = "/login";

        } else {

            alert(result.error);
        }
    });