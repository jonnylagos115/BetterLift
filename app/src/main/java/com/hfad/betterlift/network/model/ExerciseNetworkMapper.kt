package com.hfad.betterlift.network.model

import com.hfad.betterlift.domain.Exercise
import com.hfad.betterlift.domain.models.utils.EntityMapper

class ExerciseNetworkMapper : EntityMapper<ExerciseNetworkEntity, Exercise> {
    override fun mapFromEntity(entity: ExerciseNetworkEntity): Exercise {
        return Exercise(
            muscleGroupName = entity.muscle,
            exerciseName = entity.name,
            instructionList = entity.instructions
        )
    }

    override fun mapToEntity(domainModel: Exercise): ExerciseNetworkEntity {
        TODO("Not yet implemented")
    }

    fun fromEntityList(initial: List<ExerciseNetworkEntity>): List<Exercise> {
        return initial.map { mapFromEntity(it) }
    }
    //fun toEntityList(initial: List<Exercise>): List<ExerciseNetworkEntity> {}
}