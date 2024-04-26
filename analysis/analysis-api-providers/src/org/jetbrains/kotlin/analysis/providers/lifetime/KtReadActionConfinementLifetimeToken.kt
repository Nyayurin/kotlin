/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

@file:OptIn(KaAnalysisApiInternals::class)

package org.jetbrains.kotlin.analysis.providers.lifetime

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.ModificationTracker
import org.jetbrains.kotlin.analysis.api.*
import org.jetbrains.kotlin.analysis.api.lifetime.KaLifetimeToken
import org.jetbrains.kotlin.analysis.api.lifetime.KaLifetimeTokenFactory
import org.jetbrains.kotlin.analysis.providers.permissions.KaAnalysisPermissionChecker
import kotlin.reflect.KClass

public class KtReadActionConfinementLifetimeToken(
    private val project: Project,
    private val modificationTracker: ModificationTracker,
) : KaLifetimeToken() {
    private val onCreatedTimeStamp = modificationTracker.modificationCount

    override val factory: KaLifetimeTokenFactory get() = KtReadActionConfinementLifetimeTokenFactory

    override fun isValid(): Boolean {
        return onCreatedTimeStamp == modificationTracker.modificationCount
    }

    override fun getInvalidationReason(): String {
        if (onCreatedTimeStamp != modificationTracker.modificationCount) return "PSI has changed since creation."
        error("Cannot get an invalidation reason for a valid lifetime token.")
    }

    override fun isAccessible(): Boolean {
        if (!ApplicationManager.getApplication().isReadAccessAllowed) return false
        if (!KaAnalysisPermissionChecker.getInstance(project).isAnalysisAllowed()) return false

        return KaLifetimeTracker.getInstance(project).currentToken == this
    }

    override fun getInaccessibilityReason(): String {
        if (!ApplicationManager.getApplication().isReadAccessAllowed) return "Called outside a read action."

        val permissionChecker = KaAnalysisPermissionChecker.getInstance(project)
        if (!permissionChecker.isAnalysisAllowed()) return permissionChecker.getRejectionReason()

        val currentToken = KaLifetimeTracker.getInstance(project).currentToken
        if (currentToken == null) return "Called outside an `analyze` context."
        if (currentToken != this) return "Using a lifetime owner from an old `analyze` context."

        error("Cannot get an inaccessibility reason for a lifetime token when it's accessible.")
    }
}

public object KtReadActionConfinementLifetimeTokenFactory : KaLifetimeTokenFactory() {
    override val identifier: KClass<out KaLifetimeToken> = KtReadActionConfinementLifetimeToken::class

    override fun create(project: Project, modificationTracker: ModificationTracker): KaLifetimeToken =
        KtReadActionConfinementLifetimeToken(project, modificationTracker)
}
