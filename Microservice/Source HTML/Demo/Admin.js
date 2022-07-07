function getAllItem() {
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
        },
        type: 'GET',
        url: 'http://localhost:8080/manager/manage-item/items',
        success: function (items) {
            if (items.length > 0) {
                let trHeadElement = document.createElement("tr");
                let th1 = document.createElement("th");
                th1.innerHTML = "ID";
                let th2 = document.createElement("th");
                th2.innerHTML = "Name";
                let th3 = document.createElement("th");
                th3.innerHTML = "Price";
                let th4 = document.createElement("th");
                th4.innerHTML = "Quantity";
                let th5 = document.createElement("th");
                th5.innerHTML = "Supplier ID";
                let th6 = document.createElement("th");
                th6.innerHTML = "Action";
                // let th7 = document.createElement("th");
                // th7.innerHTML ="Update";
                trHeadElement.appendChild(th1);
                trHeadElement.appendChild(th2);
                trHeadElement.appendChild(th3);
                trHeadElement.appendChild(th4);
                trHeadElement.appendChild(th5);
                trHeadElement.appendChild(th6);
                // trHeadElement.appendChild(th7);
                let tHead = document.getElementById('head-table');
                tHead.appendChild(trHeadElement);
                for (i = 0; i < items.length; i++) {
                    let trElement = document.createElement("tr");
                    let td1 = document.createElement("td");
                    td1.innerHTML = items[i].id;
                    let td2 = document.createElement("td");
                    td2.innerHTML = items[i].name;
                    let td3 = document.createElement("td");
                    td3.innerHTML = items[i].price;
                    let td4 = document.createElement("td");
                    td4.innerHTML = items[i].quantity;
                    let td5 = document.createElement("td");
                    td5.innerHTML = items[i].supplierId;
                    let td6 = document.createElement("td");
                    td6.innerHTML = "<button type=" + "submit" + " " + "onclick=" + "deleteItem(" + items[i].id + ")" + ">DELETE</button>";
                    // let td7 = document.createElement("td");
                    // td7.innerHTML = "<input type=" + "text" + " " + "onclick=" + "deleteItem(" + items[i].id + ")" + "></input>";
                    trElement.appendChild(td1);
                    trElement.appendChild(td2);
                    trElement.appendChild(td3);
                    trElement.appendChild(td4);
                    trElement.appendChild(td5);
                    trElement.appendChild(td6);
                    // trElement.appendChild(td7);
                    let tbody = document.getElementById('body-table');
                    tbody.appendChild(trElement);
                }
            }
            else {
                alert("Faild!!!")
            }
        }
    })
}

function deleteItem(id) {
    console.log(id);
    $.ajax({
        type: 'DELETE',
        url: 'http://localhost:8080/manager/manage-item/delete?id=' + id,
        success: function(){
            alert("OK");
        },
        error: function(error){
            alert("Faild");
        }

    })
}

function getAllOrders() {
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
        },
        type: 'GET',
        url: 'http://localhost:8080/manager/manage-order/orders',
        success: function (orders) {
            if (orders.length > 0) {
                let trHeadElement = document.createElement("tr");
                let th1 = document.createElement("th");
                th1.innerHTML = "ID";
                let th2 = document.createElement("th");
                th2.innerHTML = "Date";
                let th3 = document.createElement("th");
                th3.innerHTML = "Store ID";
                let th4 = document.createElement("th");
                th4.innerHTML = "Total Price";
                trHeadElement.appendChild(th1);
                trHeadElement.appendChild(th2);
                trHeadElement.appendChild(th3);
                trHeadElement.appendChild(th4);
                let tHead = document.getElementById('head-table-order');
                tHead.appendChild(trHeadElement);
                for (i = 0; i < orders.length; i++) {
                    let trElement = document.createElement("tr");
                    let td1 = document.createElement("td");
                    td1.innerHTML = orders[i].id;
                    let td2 = document.createElement("td");
                    td2.innerHTML = orders[i].orderDate;;
                    let td3 = document.createElement("td");
                    td3.innerHTML = orders[i].storeId;
                    let td4 = document.createElement("td");
                    td4.innerHTML = orders[i].totalPrice;
                
                    trElement.appendChild(td1);
                    trElement.appendChild(td2);
                    trElement.appendChild(td3);
                    trElement.appendChild(td4);
                    let tbody = document.getElementById('body-table-order');
                    tbody.appendChild(trElement);
                }
            }
            else {
                alert("Faild!!!")
            }
        }
    })
}

function getOrderById() {
    let id = document.getElementById('order-id').value;
    console.log(id);
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
        },
        type: 'GET',
        url: 'http://localhost:8080/manager/manage-order/get?id='+id,
        success: function (data) {               
                let trHeadElement = document.createElement("tr");
                let th1 = document.createElement("th");
                th1.innerHTML = "ID";
                let th2 = document.createElement("th");
                th2.innerHTML = "Date";
                let th3 = document.createElement("th");
                th3.innerHTML = "Store ID";
                let th4 = document.createElement("th");
                th4.innerHTML = "Total Price";
                trHeadElement.appendChild(th1);
                trHeadElement.appendChild(th2);
                trHeadElement.appendChild(th3);
                trHeadElement.appendChild(th4);
                let tHead = document.getElementById('head-table-order-id');
                tHead.appendChild(trHeadElement);

                let trElement = document.createElement("tr");
                let td1 = document.createElement("td");
                td1.innerHTML = order.id;
                let td2 = document.createElement("td");
                td2.innerHTML = orders.orderDate;;
                let td3 = document.createElement("td");
                td3.innerHTML = orders.storeId;
                let td4 = document.createElement("td");
                td4.innerHTML = order.totalPrice;               
                trElement.appendChild(td1);
                trElement.appendChild(td2);
                trElement.appendChild(td3);
                trElement.appendChild(td4);
                let tbody = document.getElementById('body-table-order-id');
                tbody.appendChild(trElement);
                },
        error: function(error){
            alert("NO OK!!!");
        }

    })
}
//Supplier

function getAllSupplier() {
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
        },
        type: 'GET',
        url: 'http://localhost:8080/manager/manage-supplier/suppliers',
        success: function (suppliers) {
            if (suppliers.length > 0) {
                let trHeadElement = document.createElement("tr");
                let th1 = document.createElement("th");
                th1.innerHTML = "ID";
                let th2 = document.createElement("th");
                th2.innerHTML = "Name";
                let th3 = document.createElement("th");
                th3.innerHTML = "Address";
                let th4 = document.createElement("th");
                th4.innerHTML = "Phone Number";
                let th5 = document.createElement("th");
                th5.innerHTML = "Action";
                trHeadElement.appendChild(th1);
                trHeadElement.appendChild(th2);
                trHeadElement.appendChild(th3);
                trHeadElement.appendChild(th4);
                trHeadElement.appendChild(th5);
                let tHead = document.getElementById('head-table-supplier');
                tHead.appendChild(trHeadElement);
                for (i = 0; i < suppliers.length; i++) {
                    let trElement = document.createElement("tr");
                    let td1 = document.createElement("td");
                    td1.innerHTML = suppliers[i].id;
                    let td2 = document.createElement("td");
                    td2.innerHTML = suppliers[i].name;
                    let td3 = document.createElement("td");
                    td3.innerHTML = suppliers[i].address;
                    let td4 = document.createElement("td");
                    td4.innerHTML = suppliers[i].phoneNumber;
                    let td5 = document.createElement("td");
                    td5.innerHTML = "<button type=" + "submit" + " " + "onclick=" + "deleteSupplier(" + suppliers[i].id + ")" + ">DELETE</button>";
                    trElement.appendChild(td1);
                    trElement.appendChild(td2);
                    trElement.appendChild(td3);
                    trElement.appendChild(td4);
                    trElement.appendChild(td5);
                    let tbody = document.getElementById('body-table-supplier');
                    tbody.appendChild(trElement);
                }
            }
            else {
                alert("Faild!!!")
            }
        }
    })
}

function deleteSupplier(id) {
    console.log(id);
    $.ajax({
        type: 'DELETE',
        url: 'http://localhost:8080/manager/manage-supplier/delete?id=' + id,
        success: function(){
            alert("OK");
        },
        error: function(error){
            alert("NO OK");
        }

    })
}
