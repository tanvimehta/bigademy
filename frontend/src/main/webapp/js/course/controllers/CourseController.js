angular.module("courseApp").controller('courseController', function ($scope, $location, courseResource, $window, $sce) {
    $scope.queryParams = $location.absUrl().split("?")[1].split('&');
    $scope.courseId = $scope.queryParams[0].split('=')[1];
    $scope.course = {};
    $scope.displayIntro = true;
    $scope.displayModal = true;


    init = function () {
        courseResource.get({
            id: $scope.courseId
        }, function (data) {
            $scope.course = data;
            $scope.displayModal = false;
        });
    };

    init();

    // Function to go to the exercise page.
    $scope.goToFirstExercise = function () {
        $window.location = '/exercise.html?courseId=' + $scope.course.courseId +
            '&topicId=' + $scope.course.topics[0].topicId + "&subtopicId=" + $scope.course.topics[0].subtopics[0].subtopicId +
            "&exerciseId=" + $scope.course.topics[0].subtopics[0].exercises[0].exerciseId;
    };

    $scope.getHtmlSafeString = function(unsafeString) {
        return $sce.trustAsHtml(unsafeString);
    };

    $scope.toggleDisplayIntro = function () {
        $scope.displayIntro = !$scope.displayIntro;
    };
});
