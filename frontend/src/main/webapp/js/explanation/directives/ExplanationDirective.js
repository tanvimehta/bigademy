angular.module('explanationApp').directive("explanation", function ($sce) {
        return {
            restrict: 'AE',
            scope: {
                subtopic: '=',
                exercise: '='
            },
            templateUrl: '../templates/explanation.html',
            controller: function ($scope, $sce) {
                $scope.getHtmlSafeString = function (unsafeString) {
                    return $sce.trustAsHtml(unsafeString);
                };
            }
        }
    }
);