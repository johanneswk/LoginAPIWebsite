<%--
  Created by IntelliJ IDEA.
  User: john
  Date: 12/20/2018
  Time: 6:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <style>
        form {
            text-align: center;
        }
    </style>
    <meta http-equiv="content-type" content="text/html; charset=windows-1252">
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    <div class="topnav" style="text-align: center;">
        <a href="index.jsp" style="color: #040504;">Home<img src="images/Nederlandsevlag.png" style="width: 27px; height: 19px; margin-left: 125px;"></a>
        <a href="Informatie_contact.jsp" style="color: #040504;">Service &amp; Contact</a>
        <a href="#" style="margin-left: 141px; width: 131px; color: #040504;">Veilig boeken</a>
        <a href="#" style="color: #040504;">Gratis annuleren</a>
        <a href="#" style="color: #040504;">Verantwoord reizen</a>
        <a href="Mijn-Vlucht.jsp" style="color: #040504;">Mijn Vlucht</a>
    </div>
    <div style="text-align: center;"> <a href="index.jsp"><img src="images/test.png" style="width: 224px; height: 224px;"></a></div>

    <h1>Mijn Vlucht</h1>
    <br>
    <h1 style="color: red" align="center"> ${error} </h1>
    <p>Hier kunt u alle informatie over uw boeking bij ons zien</p>
    <form class="left" name="loginForm method=" method="post" action="flight-info">
            Ticketnummer: <br> <input type="text" name="ticket-nummer" align="left"/> <br>
            Vluchtummer: <br> <input type="text" name="vlucht-nummer" align="left"> <br>
            Email: <br> <input type="email" name="email" align="left"/> <br>
            Wachtwoord: <br> <input type="password" name="wachtwoord" align="left"/>  <br>
            <input type="submit" value="Login" />
    </form>
</body>
</html>

