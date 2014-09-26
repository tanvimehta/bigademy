var masthead = angular.module('mastheadModule', ['ngResource', 'ngRoute', 'ui.bootstrap']).directive('masthead',function () {
    return {
        restrict: 'AE',
        templateUrl: '../templates/masthead.html',
        scope: {
            course: '='
        }
    }
}).run(function ($window) {
    var addressBar = document.location.href;

    function getCookie(cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i].trim();
            if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
        }
        return "";
    }

    var cookie = getCookie("authInfo");
    if (cookie == "" && addressBar.indexOf("frontpage.html") < 0) {
        $window.location = "frontpage.html";
    }
});

// resource for subtopic change rest call.
masthead.factory('userVerify', function ($resource) {
    return $resource('/api/user/verify', {}, {});
});

masthead.controller("mastheadController", function ($scope, $modal, $window) {
        $scope.auth = {
            success: false,
            errorMessage: '',
            user: {}
        };

        /**
         * function to get a cookie
         */
        function getCookie(cname) {
            var name = cname + "=";
            var ca = document.cookie.split(';');
            for (var i = 0; i < ca.length; i++) {
                var c = ca[i].trim();
                if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
            }
            return "";
        }

        $scope.goToHomePage = function () {
            $window.location = "/home.html";
        }

        $scope.goToCourse = function (courseId) {
            $window.location = '/course.html?courseId=' + courseId;
        }

        $scope.init = function () {
            var auth = getCookie("authInfo");
            if (auth != "") {
                $scope.auth = angular.fromJson(auth);
            }
        }

        $scope.init();

        $scope.signInModalOpen = function () {
            $scope.auth.errorMessage = '';
            var modalInstance = $modal.open({
                templateUrl: 'sign-in.html',
                controller: ModalInstanceCtrl,
                resolve: {
                    auth: function () {
                        return $scope.auth;
                    }
                }
            })
        }
        /**
         * function which sets a cookie.
         */
        $scope.setCookie = function setCookie(cname, cvalue) {
            var d = new Date();
            // Setting the expiration date to 10 years from now.
            d.setTime(d.getTime() - (10 * 365 * 24 * 60 * 60 * 1000));
            var expires = "expires=" + d.toGMTString();
            document.cookie = cname + "=" + cvalue + "; " + expires;
        }

        $scope.signOut = function () {
            $scope.setCookie("authInfo", angular.toJson($scope.auth));
            $scope.auth = {
                success: false,
                errorMessage: '',
                user: {}
            };
            $window.location = "/frontpage.html";
        }
    }
);

// Please note that $modalInstance represents a modal window (instance) dependency.
// It is not the same as the $modal service used above.

var ModalInstanceCtrl = function ($scope, $modalInstance, $window, auth, $http, userVerify) {

    $scope.credentials = {
        email: '',
        password: ''
    };

    $scope.auth = auth;

    $scope.signIn = function () {
        userVerify.get({
            email: $scope.credentials.email,
            password: $scope.credentials.password
        }, function (data) {
            if (data) {
                $scope.auth.success = true;
                $scope.auth.errorMessage = '';
                $scope.auth.user = data;
                $scope.setCookie("authInfo", angular.toJson($scope.auth));
                $scope.cancel();
                $window.location = "/home.html";
            }
        }, function () {
            $scope.auth.errorMessage = "Invalid Username or Password.";
        });
    }

    $scope.cancel = function () {
        $scope.auth.errorMessage = '';
        $modalInstance.dismiss('cancel');
    };

    /**
     * function which sets a cookie.
     */
    $scope.setCookie = function setCookie(cname, cvalue) {
        var d = new Date();
        // Setting the expiration date to 10 years from now.
        d.setTime(d.getTime() + (10 * 365 * 24 * 60 * 60 * 1000));
        var expires = "expires=" + d.toGMTString();
        document.cookie = cname + "=" + cvalue + "; " + expires;
    };
}

