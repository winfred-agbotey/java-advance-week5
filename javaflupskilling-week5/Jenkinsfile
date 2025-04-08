pipeline {
	agent any

    environment {
		MY_ENV_VAR = 'Custom Value'
        DB_URL = credentials('DB_URL')
        POSTGRES_DB = credentials('POSTGRES_DB')
        DB_PASSWORD = credentials('DB_PASSWORD')
        DB_USERNAME = credentials('DB_USERNAME')
        DOCKERHUB_CREDENTIALS_PSW = credentials('DOCKERHUB_CREDENTIALS_PSW') // Docker Hub password
        DOCKERHUB_CREDENTIALS_USR = credentials('DOCKERHUB_CREDENTIALS_USR') // Docker Hub username
        DOCKER_IMAGE = 'mawulidev/javaflupskilling-week5' // Docker image name
        DOCKER_TAG = 'latest' // Docker image tag
    }

    stages {
		stage('Checkout') {
			steps {
				script {
					checkout([$class: 'GitSCM',
                        branches: [[name: '*/main']],
                        userRemoteConfigs: [[url: 'https://github.com/winfred-agbotey/javaflupskilling-week5.git']],
                        extensions: [[$class: 'CleanBeforeCheckout'], [$class: 'CloneOption', noTags: false, shallow: true, depth: 1]],
                    ])
                }
            }
        }

        stage('Prepare Environment') {
			steps {
				configFileProvider([configFile(fileId: 'env-file', targetLocation: '.env')]) {
					sh 'cat .env' // Verify placement of the .env file
                }
            }
        }

        stage('Build') {
			steps {
				sh '''
                echo "In Build Step..."
                mvn clean install
                '''
            }
        }

        stage('Test') {
			steps {
				sh '''
                echo "In Test Step..."
                mvn test
                '''
            }
        }

        stage('Docker Build and Push') {
			input {
				message 'Do You Want to Deply?'
				ok 'Yes Deploy!'
				parameters {
					string(name: 'TARGET_ENVIRONMENT', defaultValue: 'PROD', description: 'Target deployment environment')
				}
			}
			steps {
				withCredentials([string(credentialsId: 'DOCKERHUB_CREDENTIALS_USR', variable: 'DOCKERHUB_CREDENTIALS_USR'), string(credentialsId: 'DOCKERHUB_CREDENTIALS_PSW', variable: 'DOCKERHUB_CREDENTIALS_PSW')]) {
					sh '''
                    echo "Building Docker image..."
                    docker build -t $DOCKER_IMAGE:$DOCKER_TAG .

                    echo "Logging in to Docker Hub..."
                    echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin

                    echo "Pushing Docker image to Docker Hub..."
                    docker push $DOCKER_IMAGE:$DOCKER_TAG
                    '''
                }
            }
        }
    }

    post {
		success {
			echo 'Pipeline succeeded!'
        }
        failure {
			echo 'Pipeline failed!'
        }
    }
}
