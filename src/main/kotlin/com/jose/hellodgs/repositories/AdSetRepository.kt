package com.jose.hellodgs.repositories

import com.jose.hellodgs.entities.AdSet
import org.springframework.data.repository.CrudRepository
import java.util.*

interface AdSetRepository : CrudRepository<AdSet?, Long?> {
    fun findByAdvertiserName(name: String): AdSet?
    // Column name is deducted from method name not from parameters.
    fun findAllByAdvertiserName(name: String): List<AdSet?>
    override fun findById(id: Long): Optional<AdSet?>
}
