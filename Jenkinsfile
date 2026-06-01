pipeline {
    agent any

    tools {
        maven 'maven-3.9.15'
    }

    parameters {
        string(name: 'DEPLOY_HOST', defaultValue: '', description: 'Target deployment server')
        string(name: 'DEPLOY_PASS', defaultValue: '', description: 'Password deployment server')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'integrate-jenkins',
                    url: 'https://github.com/avrdude07/wms-service.git'
            }
        }

        stage('Check User') {
            steps {
                sh '''
                    whoami
                    pwd
                '''
            }
        }

        stage('Check Tools') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    scp wms-web/target/*.war hank@${DEPLOY_HOST}:/spring-project/wmsapp/
                    ssh hank@${DEPLOY_HOST} "echo '${DEPLOY_PASS}' | sudo -S systemctl restart wms-api"
                '''
            }
        }
    }
}