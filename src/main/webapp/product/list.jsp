<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.javaservletjsp.entity.Product" %>
<%@ page import="com.example.javaservletjsp.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ArrayList<Product> list = (ArrayList<Product>) request.getAttribute("products");

    if (list == null) {
        list = new ArrayList<>();
    }

    User account = (User) session.getAttribute("currentUser");
    boolean login = false;
    if (account != null) {
        login = true;
    }
    String Username = account == null ? "Guest" : account.getUsername();
%>
<html>
<head>
    <title>Foods</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
</head>
<body>
<ul class="nav align-items-center float-right">
    <li class="nav-item float-right">
        <% if (login) {
        %><%=Username%> <a href="/logout">(Logout)</a>
        <%
        } else {%>
        <a href="/login">Login</a>
        <%}%>
    </li>
</ul>
<div class="container">
    <a href="/admin/foods/create" class="btn btn-outline-primary float-right">Create</a>
    <table class="table table-striped">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">Name</th>
            <th scope="col">Amount</th>
            <th scope="col">Price</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Product food : list
            ) {%>
        <tr>
            <td><%=food.getId()%>
            </td>
            <td><%=food.getName()%>
            </td>
            <td><%=food.getAmount()%>
            </td>
            <td><%=food.getPrice()%> VND</td>


        </tr>
        <%}%>
        </tbody>
    </table>

</div>
</body>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        let listDeleteBtn = document.querySelectorAll('.btn-delete');
        for (let i = 0; i < listDeleteBtn.length; i++) {
            listDeleteBtn[i].addEventListener('click', function (event) {
                event.preventDefault();
                if (confirm("Are U sure?")) {
                    let xhr = new XMLHttpRequest();
                    xhr.onreadystatechange = function () {
                        if (xhr.readyState == 4 && xhr.status == 200) {
                            alert("Deleted!")
                            window.location.reload()
                        }
                    }
                    xhr.open('DELETE', this.href, false);
                    xhr.send();

                }
            })
        }
    })
</script>
</html>
