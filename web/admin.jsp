<!doctype html>
<%@page import="models.User"%>
<%@page import="models.Trip"%>
<%@page import="java.util.ArrayList"%>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <!--Material Icons-->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Outlined" rel="stylesheet">
    <script type="module" src="https://unpkg.com/ionicons@5.5.2/dist/ionicons/ionicons.esm.js"></script>
    <script nomodule src="https://unpkg.com/ionicons@5.5.2/dist/ionicons/ionicons.js"></script>
    <!--fonts-->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat&family=Prosto+One&display=swap" rel="stylesheet">

    <title>Minicab</title>
  </head>
    <body>
        <!-- navbar -->
        <nav class="navbar navbar-dark bg-dark">
            <h4 class="text-center text-warning logo">MINIC<span class="material-icons-outlined text-warning mt-2">local_taxi</span>B
            </h4>
            <ul class="Login">
                <li><a href="register?role=customer">Sign Up</a></li>
                <li><a href="index.html">Login</a></li>
                <li><a href="index.html"><%= session.getAttribute("username") %></a></li>
                </ul>
            </div>
         </nav>
        <div class="container pa-4">
            <h1 style="margin-top: 60px;">Admin</h1>
            <a style="margin-bottom: 60px;" href="register?role=driver" class="btn btn-dark btn-lg active float-right" role="button" aria-pressed="true">Add Driver</a>
            <h4 class="my-4">Users</h4>
            <form action="admin" method="post">
            <table class="table mt-4" style="margin-bottom: 100px;">
                <thead>
                  <tr>
                    <th scope="col">#</th>
                    <th scope="col">Username</th>
                    <th scope="col">Role</th>
                    <th scope="col"></th>
                  </tr>
                </thead>
                <tbody>
                    <% ArrayList<User> users = (ArrayList<User>) request.getAttribute("nonAdmins");
                for (int i = 0; i < users.size(); i++) {%>
                  <tr>
                    <th scope="row"><%= users.get(i).getId()%></th>
                    <td><%= users.get(i).getUsername()%></td>
                    <td><%= users.get(i).getRole()%></td>
                    <td>
                        <% if (users.get(i).getRole().equals("driver")) { %>
                            <button name="remove" value="<%= users.get(i).getId() %>" act type="submit" class="btn btn-dark">Remove</button>
                        <% } %>
                    </td>
                  </tr>
                  <% }%> 
                </tbody>
              </table>
            </form>
            <h4 class="my-4">Trips</h4>
            <table class="table">
                <thead>
                  <tr>
                    <th scope="col">Pickup Location</th>
                    <th scope="col">Destination</th>
                    <th scope="col">Status</th>
                    <th scope="col">Request Time</th>
                    <th scope="col">Total Amount</th>
                  </tr>
                </thead>
                <tbody>
                    <% ArrayList<Trip> trips = (ArrayList<Trip>) request.getAttribute("trips");
                for (int i = 0; i < trips.size(); i++) {%>
                  <tr>
                    <th scope="row"><%= trips.get(i).getPickupLoc()%></th>
                    <td><%= trips.get(i).getDestinationLoc()%></td>
                    <td><%= trips.get(i).getStatus()%></td>
                    <td><%= trips.get(i).getRequestTime()%></td>
                    <td><%= trips.get(i).getPayment().getTotalAmt()%></td>
                  </tr>
                  <% }%> 
                </tbody>
              </table>
        </div>
     </div>




</head>
        
        <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
        
    </body>
</html>

<!-- css -->
<style>
    body {
        margin: 0;
        padding: 0;
        background-color: #ffc107;
        height: 100vh;
        font-family: 'Montserrat', sans-serif;
    }

    .requests{
        margin:80px 130px;
        height: 70%;
        align-items: center;
        color: #212529;
        position: absolute;
        top: 70%;
        left: 8%;
        transform: translateY(-50%);
        z-index: 2;

    }


    .wrapper{
        margin:80px 130px;
        height: 70%;
        align-items: center;
        color: #212529;
        position: absolute;
        top: 50%;
        left: 8%;
        transform: translateY(-50%);
        z-index: 2;
    }

    .wrapper .button{
        display: inline-block;
        height: 60px;
        width: 60px;
        float: left;
        margin: 0 5px;
        overflow: hidden;
        background: white;
        border-radius: 50px;
        cursor: pointer;
        box-shadow: 0px 10px 10px rgba(0,0,0,0.1);
        transition: all 0.3s ease-out;

    }

    .wrapper .button:hover{
        width: 200px;
    }

    .wrapper .button .icon{
        display: inline-block;
        height: 60px;
        width: 60px;
        text-align: center;
        border-radius: 50px;
        box-sizing: border-box;
        line-height: 60px;
    }

    .wrapper .button:nth-child(1):hover .icon{
        background:#ffc107;
    }

    .wrapper .button:nth-child(2):hover .icon{
        background:#ffc107;
    }

    .wrapper .button:nth-child(3):hover .icon{
        background:#ffc107;
    }

    .wrapper .button:nth-child(4):hover .icon{
        background:#ffc107;
    }

    .wrapper .button .icon i{
        font-size: 25px;
        line-height: 60px;
        transition: all 0.3s ease-out;
    }

    .wrapper .button .span{
        font-size: 20px;
        font-weight: 500;
        line-height: 60px;
        margin-left: 10px;
    }

    .Login{
        margin-left: 40px;
    }

    .nav-links{
        flex:1;
    }

    .nav-links ul{
        margin-left: 50px;
        display: inline;
        padding: 8px 25px;
    }

    nav ul li{
        list-style: none;
        display: inline-block;
        margin-left: 60px;
    }

    nav ul li a{
        text-decoration: none;
        color:#ffc107;
    }

    h4 {
        font-family: 'Prosto One', cursive !important;
    }
</style>
