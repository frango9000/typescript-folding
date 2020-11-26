package com.github.fsancheztemprano.typescriptfolding.services

import com.intellij.openapi.project.Project
import com.github.fsancheztemprano.typescriptfolding.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
