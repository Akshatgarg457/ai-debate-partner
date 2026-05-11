async function login() {

    const username =
        document.getElementById("username").value;

    const password =
        document.getElementById("password").value;

    const response = await fetch("/auth/login", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({
            username,
            password
        })
    });

    const data = await response.json();

    if(data.success){

        localStorage.setItem(
            "username",
            username
        );

        alert("Login successful");

        window.location.href =
            "/dashboard";

    } else {

        alert(data.error);
    }
}