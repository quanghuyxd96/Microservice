function signup(e) {
    event.preventDefault();
    var username = document.getElementById("username").value;
    var email = document.getElementById("email").value;
    var password = document.getElementById("password").value;
    var name = document.getElementById("name").value;
    var confirmPassword = document.getElementById("confirmPassword").value;
    var address = document.getElementById("address").value;
    var email = document.getElementById("phoneNumber").value;

    var store = {
        userName: userName,
        password: password,
        confirmPassword: confirmPassword,
        name: name,
        address : address,
        phoneNumber : phoneNumber,
        email : email
    };
    var json = JSON.stringify(user);
    localStorage.setItem(username, json);

    alert("dang ky thanh cong");
  }

  const list = document.getElementById('list');
        api_url='http://localhost:8080/store/save';
        async function get(){
            const response = await fetch(api_url);
            const results = await response.json();
            
            // results.forEach(result => {
            //     console.log(result);
            //     const divStore =document.createElement('div');
            //     divStore.innerHTML='<p> ${result.name}</p> <p> ${result.address}</p>';
            //     list.appendChild(divStore);               
            // });
            console.log(results);
        }
        get();
//   function login(e) {
//     event.preventDefault();
//     var username = document.getElementById("username").value;
//     var email = document.getElementById("email").value;
//     var password = document.getElementById("password").value;
//     var user = localStorage.getItem(username);
//     var data = JSON.parse(user);
//     if (!user) {
//       alert("vui long nhap username password");
//     } else if (
//       username == data.username &&
//       email == data.email &&
//       password == data.password
//     ) {
//       alert("dang nhap thanh cong");
//       window.location.href = "todolist.html";
//     } else {
//       alert("dang nhap that bai");
//     }
//   }