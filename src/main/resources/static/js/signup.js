document
.getElementById("signupForm")

.addEventListener("submit", async function(e){

    e.preventDefault();

    let fullName =
        document.getElementById("fullName").value;

    let email =
        document.getElementById("email").value;

    let username =
        document.getElementById("username").value;

    let password =
        document.getElementById("password").value;

    let confirmPassword =
        document.getElementById("confirmPassword").value;

    let securityQuestion =
        document.getElementById("securityQ").value;

    let securityAnswer =
        document.getElementById("answer").value;

    try {

        const response =
            await fetch("/auth/signup", {

                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({

                    fullName,
                    email,
                    username,
                    password,
                    confirmPassword,
                    securityQuestion,
                    securityAnswer
                })
            });

        const data =
            await response.json();

        if(data.success){

            alert(
                "Account created successfully"
            );

            window.location.href =
                "/login";
        }

        else{

            alert(data.error);
        }

    } catch(error){

        console.error(error);

        alert("Signup failed");
    }
});