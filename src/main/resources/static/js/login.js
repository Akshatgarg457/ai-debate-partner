// =========================
// LOGIN FORM
// =========================
const form =
    document.getElementById("loginForm");

// =========================
// SAVE USERNAME
// =========================
form.addEventListener("submit", function(){

    const username =
        document.getElementById("username").value;

    // SAVE USERNAME
    localStorage.setItem("username", username);
});