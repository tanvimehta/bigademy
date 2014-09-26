/**
 * Created by mshah on 3/16/14.
 */
var courseApp = angular.module("courseApp", ['ngResource', 'ngSanitize', 'mastheadModule', 'contentIndexModule', 'loadModalApp']);

//resource for pig running rest call.
courseApp.factory('courseResource', function ($resource) {
    return $resource('/api/courses/:id', {}, {});
});