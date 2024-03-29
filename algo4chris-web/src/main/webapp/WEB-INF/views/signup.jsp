<%--
  Created by IntelliJ IDEA.
  User: chris
  Date: 2021/10/19
  Time: 下午 11:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix='form' uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>{ ㄏ; }</title>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.js'/>"></script>
    <link rel='stylesheet' href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" />
    <script type="text/javascript" src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
</head>
<style>

</style>

<script>
    $(document).ready(function () {

        //domain & api url
        let httpDomain   = "http://localhost:8080/";
        let signUpUrl    = "/api/signUpAccount";
        let homeUrl = "home";

        //响应参数
        let response_success = "0000";

        /**
         * 註冊用戶
         *
         * */
        $("#submitSignup").click(function (e) {
            e.preventDefault();
            var memberName = $("#memberName").val();
            var email      = $("#email").val();
            var pwd        = $("#password").val();
            var data = {
                "memberName": memberName,
                "email": email,
                "password": pwd
            };
            $.ajax({
                method:"POST",
                url: signUpUrl,
                contentType: 'application/json; charset=utf-8',
                data : JSON.stringify(data),
                dataType:'JSON',
                success: function (result) {
                    console.log(result);
                    if(result.code === response_success){
                        //重導向到首頁
                        var jwtToken = result.data.token;
                        var mName = result.data.memberName;
                        var rToken = result.data.refreshToken;
                        var redirectUrl = httpDomain+homeUrl+"?Authorization=Bearer "+jwtToken
                            +"&memberName="+mName
                            +"&refreshToken="+rToken;
                        console.log(redirectUrl);
                        window.location.href=redirectUrl;
                    } else {
                        console.log(result.message);
                    }
                },
                error: function (result) {
                    console.log(result);
                }
            });
        });



    });
</script>

<body>

<section class="vh-100" style="background-color: #333">
    <div class="container py-5 h-100">
        <div class="row d-flex justify-content-center align-items-center h-100">
            <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                <div class="card shadow-2-strong" style="border-radius: 1rem;">
                    <div class="card-body p-5 text-center" >

                        <h3 class="mb-5" style="font-weight: bold">Welcome</h3>

                        <div class="form-outline mb-4">
                            <input type="text" id="memberName" class="form-control form-control-lg" />
                            <label class="form-label" for="memberName" style="font-weight: bold;font-size: large">User</label>
                        </div>

                        <div class="form-outline mb-4">
                            <input type="email" id="email" class="form-control form-control-lg" />
                            <label class="form-label" for="email" style="font-weight: bold;font-size: large">Email</label>
                        </div>

                        <div class="form-outline mb-4">
                            <input type="password" id="password" class="form-control form-control-lg" />
                            <label class="form-label" for="password" style="font-weight: bold;font-size: large">Password</label>
                        </div>

                        <!-- Checkbox -->
                        <div class="form-check d-flex justify-content-start mb-4">
                            <input
                                    class="form-check-input"
                                    type="checkbox"
                                    value=""
                                    id="form1Example3"
                            />
                            <label class="form-check-label" for="form1Example3"> Remember password </label>
                        </div>

                        <button class="btn btn-primary btn-lg btn-block" type="submit" id="submitSignup">Sign up</button>

                        <hr class="my-4">
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>



</body>
</html>
