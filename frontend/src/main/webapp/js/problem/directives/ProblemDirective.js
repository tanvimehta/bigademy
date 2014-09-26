angular.module('problemApp').directive("problem", function ($sce) {
        return {
            restrict: 'AE',
            scope: {
                exercise: '='
            },
            templateUrl: '../templates/problem.html',
            controller: function ($scope, $sce) {
                $scope.getHtmlSafeString = function (unsafeString) {
                    return $sce.trustAsHtml(unsafeString);
                }
                $scope.getFormattedDataset = function (dataset) {
                    var lines = dataset.split("\n");
                    var formattedDataSet = '';
                    for (i = 0; i < lines.length; i++) {
                        formattedDataSet += "&nbsp " + lines[i] + "<br/>";
                    }
                    return formattedDataSet;
                };


            }
        }
    }
);