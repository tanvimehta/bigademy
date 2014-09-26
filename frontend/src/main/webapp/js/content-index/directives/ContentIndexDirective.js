angular.module('contentIndexModule').directive('contentIndex', function() {
    return {
        restrict: 'AE',
        templateUrl: '../../../templates/content-index.html',
        scope: {
            course: '='
        },
        controller: function($scope, $window) {
            $scope.goToExercisePage = function(topicId, subtopicId, exerciseId) {
                $window.location = '/exercise.html?courseId=' + $scope.course.courseId + '&topicId=' + topicId +
                    '&subtopicId=' + subtopicId + "&exerciseId=" + exerciseId;
            }
        }
    }
});