/**
 * Created by mshah on 3/17/14.
 */
var homeApp = angular.module("homeApp", ['mastheadModule',  'ngResource']);

//resource for pig running rest call.
homeApp.factory('courseResource', function ($resource) {
    return $resource('/api/courses/all', {}, {});
});

homeApp.controller("homeController", function ($scope, $window, courseResource) {
   $scope.courses = {};
   $scope.slides = [{image: 'images/screen_shot_1.png'}, {image: 'images/screen_shot_2.png'}, {image: 'images/screen_shot_3.png'}, {image: 'images/screen_shot_4.png'}];
   // Function to initialize the course information.
   $scope.init = function () {
       courseResource.get({}, function (data) {
           $scope.courses = data.courses;
       });
   }

    // Function to go to the courses page.
   $scope.goToCourse = function(courseId) {
       $window.location = '/course.html?courseId=' + courseId;
   }
});
