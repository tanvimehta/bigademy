angular.module('codeEditorApp').directive('codeEditor', function ($document, $http) {
    return {
        restrict: 'AE',
        scope: {
            isExerciseClicked: '=',
            loadStatement: '=',
            passToBackend: '=',
            exerciseId: '='
        },
        templateUrl: '../templates/code-editor.html',
        controller: function ($scope, $sce) {

            $scope.codeRunResult = {
                success: true,
                errorMessage: ''
            };
            $scope.codeEditorModel = {
                sce: $sce,
                code: '',
                successFullRun: false,
                isCodeRunning: false
            };

            $scope.resetVar = function () {
                $scope.codeRunResult = {
                    success: true,
                    errorMessage: ''
                };
                $scope.codeEditorModel = {
                    sce: $sce,
                    code: '',
                    successFullRun: false,
                    isCodeRunning: false
                };
            };

            $scope.$on("reset-code-editor", function () {
                $scope.resetVar();
            });

            $scope.formatLoadStatement = function (loadStatement) {
                if (!angular.isUndefined(loadStatement)) {
                    var lines = loadStatement.split(";");
                    var formattedLoadStatement = '';
                    for (i = 0; i < lines.length; i++) {
                        if (lines[i] != '') {
                            formattedLoadStatement += lines[i] + ";<br/>";
                        }
                    }
                    return formattedLoadStatement;
                }
            }

            $scope.handlePassToBackend = function (code) {
                if (code == '') {
                    $scope.codeRunResult = {
                        success: false,
                        errorMessage: "Enter your solution in the text editor. If you need help, please use the hints provided or take a look at the solution!"
                    };
                } else {
                    $scope.handleCallToCodeExecution(code);
                }
            }

            $scope.handleCallToCodeExecution = function (code) {
                $scope.codeEditorModel.isCodeRunning = true;
                $http({method: 'GET', url: "/api/pigrun", params: {
                    exerciseId: $scope.exerciseId,
                    pigScript: code
                }
                }).success(function (data) {
                    $scope.codeRunResult = data;
                    $scope.codeEditorModel.isCodeRunning = false;
                    $scope.codeEditorModel.successFullRun = true;
                }).error(function () {
                    $scope.codeEditorModel.isCodeRunning = false;
                });
            }

            $scope.handleNonBackendCodeExecution = function (code) {
                if (code == '') {
                    $scope.codeRunResult = {
                        success: true,
                        errorMessage: ''
                    };
                    $scope.codeEditorModel.successFullRun = true;
                } else {
                    $scope.codeRunResult = {
                        success: false,
                        errorMessage: "The solution for this exercise has been provided. Please erase all the text in the text area and click on the Run button above."
                    }
                }
            }

            // function to execute the code
            $scope.runCode = function (code) {
                if ($scope.passToBackend) {
                    $scope.handlePassToBackend(code);
                } else {
                    $scope.handleNonBackendCodeExecution(code);
                }
            }
        },
        link: function (scope) {
            scope.$watch('isExerciseClicked', function (newVal) {
                if (newVal) {
                    scope.isExerciseClicked = false;
                    document.getElementById("codeArea").focus();
                }
            });
        }

    }
});