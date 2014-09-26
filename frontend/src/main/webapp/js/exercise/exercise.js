var exerciseApp;
exerciseApp = angular.module('exerciseApp', [ 'ngResource', 'ngRoute', 'ngSanitize', 'ui.bootstrap', 'mastheadModule',
    'contentIndexModule', 'codeEditorApp', 'loadModalApp', 'explanationApp', 'problemApp']).config(function ($locationProvider) {
    $locationProvider.html5Mode(true);
});

// resource for subtopic change rest call.
exerciseApp.factory('subTopicResource', function ($resource) {
    return $resource('/api/subtopics/:id', {}, {});
});

//resource for pig running rest call.
exerciseApp.factory('courseResource', function ($resource) {
    return $resource('/api/courses/:id', {}, {});
});

