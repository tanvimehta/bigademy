frontPageApp = angular.module('frontPageApp', [ 'ngResource', 'ngRoute', 'mastheadModule' ])

// resource for subtopic change rest call.
masthead.factory('addToWaitListResource', function ($resource) {
    return $resource('/api/waitList/add', null, {
        'addEmailToWaitList': {method: 'PUT'}
    });
});

frontPageApp.controller("frontPageController", function ($scope, $http) {
    $scope.frontPageParam = {
        waitListEmail: '',
        added: false
    };
    $scope.slides = [{image: 'images/screen_shot_1.png'}, {image: 'images/screen_shot_2.png'}, {image: 'images/screen_shot_3.png'}, {image: 'images/screen_shot_4.png'}];
    $scope.addToWaitList = function () {
        $http({method:"PUT",url:'/api/waitList/add', params:{
            email: $scope.frontPageParam.waitListEmail
        }});
        $scope.frontPageParam.added = true;
    };
});
