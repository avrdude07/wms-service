pipeline {
    agent any

    tools {
        maven 'maven-3.9.15'
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
                    scp wms-web/target/*.war hank@192.168.84.128:/spring-project/wmsapp/
                    ssh hank@192.168.84.128 "echo 'P@ssw0rd' | sudo -S systemctl restart wms-api"
                '''
            }
        }
    }
}