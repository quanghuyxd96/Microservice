function signUp() {
    var userName = document.getElementsByName("userName").value;
    var password = document.getElementsByName("password").value;
    var confirmPassword = document.getElementsByName("confirmPassword").value;
    var name = document.getElementsByName("name").value;
    var address = document.getElementsByName("address").value;
    var phoneNumber = document.getElementsByName("phoneNumber").value;
    var email = document.getElementsByName("email").value;
    var store = {
        userName: userName,
        password: password,
        confirmPassword: confirmPassword,
        name: name,
        address : address,
        phoneNumber : phoneNumber,
        email : email
    }
    var json = JSON.stringify(store);
    localStorage.setItem(userName,json);
    alert("OK");
}