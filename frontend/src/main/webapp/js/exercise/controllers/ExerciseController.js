// Main exercise page controller.
angular.module('exerciseApp').controller("exerciseController", function ($scope, $rootScope, $location, subTopicResource,
                                                                          courseResource, $window) {
    $scope.queryParams = document.location.href.split("?")[1].split('&');
    $scope.courseId = $scope.queryParams[0].split('=')[1];
    $scope.topicId = $scope.queryParams[1].split('=')[1];
    $scope.subTopicId = $scope.queryParams[2].split('=')[1];
    $scope.exerciseId = $scope.queryParams[3].split('=')[1];

    $scope.currExerciseIndex = 0;

    $scope.firstExerciseId = -1;
    $scope.lastExerciseId = -1;

    $scope.subTopic = {};
    $scope.exercise = {};
    $scope.course = {};

    $scope.subTopicResource = subTopicResource;
    $scope.courseResource = courseResource;

    $scope.isExerciseClicked = false;

    $scope.displayModal = true;

    $scope.showIndex = false;
    $scope.sideNavBarOption = ['explanation', 'exercise'];
    $scope.selectedSideNavBarOption = 'explanation';

    $scope.changeSelectedNavBarOption = function(option) {
        $scope.selectedSideNavBarOption = option;
    }

    $scope.indexToggle = function() {
        $scope.showIndex = !$scope.showIndex;
    }

    // Function that handles fetching all the information for the current course.
    var fetchCoureseInformation = function () {
        $scope.courseResource.get({
            id: $scope.courseId
        }, function (data) {
            $scope.course = data;
            $scope.firstExerciseId = $scope.course.topics[0].subtopics[0].exercises[0].exerciseId;
            lastTopic = $scope.course.topics[$scope.course.topics.length - 1];
            lastSubTopic = lastTopic.subtopics[lastTopic.subtopics.length - 1];
            $scope.lastExerciseId = lastSubTopic.exercises[lastSubTopic.exercises.length - 1].exerciseId;
            setCurrExerciseIndex();
            changeSubtopic();
        });
    };

    setCurrExerciseIndex = function () {

        angular.forEach($scope.course.topics, function (topics, key1) {
            angular.forEach(topics.subtopics, function(subtopic, key2) {
                angular.forEach(subtopic.exercises, function(exercise, key3) {
                    if( exercise.exerciseId == $scope.exerciseId) {
                        $scope.currExerciseIndex = key3;
                    }
                });
            });

        });
    }

    // Function which changes the subtopic
    changeSubtopic = function () {
        $scope.displayModal = true;
        // make a call to get next subtopic.
        $subTopic = $scope.subTopicResource.get({
            id: $scope.subTopicId
        }, function (data) { // call back function to handle subtpoic change.
            // change the subtopic data.
            $scope.subTopic = data;
            // change the exercise.
            changeExercise();
        });
    };

    var init = function () {
        fetchCoureseInformation();
    };

    init();

    // Function to change the subtopic.
    changeExercise = function () {
        $scope.exercise = $scope.subTopic.exercises[$scope.currExerciseIndex];
        $scope.exerciseId = $scope.exercise.exerciseId;

        $window.history.pushState('', 'Title',
                "exercise.html?courseId=" + $scope.courseId +
                "&topicId=" + $scope.topicId + "&subtopicId=" + $scope.subTopicId + "&exerciseId=" + $scope.exerciseId);

        $rootScope.$broadcast("reset-code-editor");
        $scope.displayModal = false;
    };

    $scope.prevExercise = function () {
        if ($scope.currExerciseIndex > 0) {
            $scope.currExerciseIndex = $scope.currExerciseIndex - 1;
            changeExercise();
        } else if ($scope.firstExerciseId == $scope.exerciseId) {
            $scope.forwardToCoursePage();
        } else {
            $scope.goToPrevSubtopic();
        }
    };

    $scope.forwardToCoursePage = function () {
        $window.location = '/course.html?courseId=' + $scope.course.courseId;
    };

    // Handles going to previous subtopic in case of prev action.
    $scope.goToPrevSubtopic = function () {
        var foundSubtopic = false;
        var foundPrevSubtopic = false;
        var prevTopic = -1;
        var prevSubtopic = {};
        angular.forEach($scope.course.topics, function (topic, key) {
            angular.forEach(topic.subtopics, function (subTopic, key) {
                if (subTopic.subtopicId == $scope.subTopicId) {
                    foundSubtopic = true;
                }
                if (!foundSubtopic) {
                    prevTopic = topic.topicId;
                    prevSubtopic = subTopic;
                    foundPrevSubtopic = true;
                }
            });
        });

        $scope.subTopicId = prevSubtopic.subtopicId;
        $scope.topicId = prevTopic;
        $scope.exerciseId = prevSubtopic.exercises[prevSubtopic.exercises.length - 1].exerciseId;
        $scope.currExerciseIndex = prevSubtopic.exercises.length - 1;
        changeSubtopic();

    };


    // function to handle next exercise call
    $scope.nextExercise = function () {
        if ($scope.currExerciseIndex < $scope.subTopic.exercises.length - 1) {
            $scope.currExerciseIndex = $scope.currExerciseIndex + 1;
            changeExercise();
        } else if ($scope.lastExerciseId == $scope.exerciseId) {
            $scope.forwardToCoursePage();
        } else {
            $scope.goToNextSubtopic()
        }
    };

    // Handles going to previous subtopic in case of next action.
    $scope.goToNextSubtopic = function () {
        var foundSubtopic = false;
        var foundNextSubtopic = false;

        angular.forEach($scope.course.topics, function (topic, key) {
            angular.forEach(topic.subtopics, function (subTopic, key) {
                if (foundSubtopic && !foundNextSubtopic) {
                    $scope.subTopicId = subTopic.subtopicId;
                    $scope.topicId = topic.topicId;
                    $scope.exerciseId = subTopic.exercises[0].exerciseId;
                    $scope.currExerciseIndex = 0;
                    foundNextSubtopic = true;
                }

                if (subTopic.subtopicId == $scope.subTopicId) {
                    foundSubtopic = true;
                }
            });
        });
        changeSubtopic();
    };
});