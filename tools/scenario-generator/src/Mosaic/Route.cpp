//
// Created by parallels on 5/26/21.
//

#include "Route.h"

#include <loguru.hpp>
#include "SQLiteCpp/SQLiteCpp.h"

Route::Route(const Position& source, const Position& target) : source(source), target(target) {}
Route::Route(int id, const Position &source, const Position &target) : id(id), source(source), target(target) {}

bool Route::operator==(const Route &rhs) const {
    return id == rhs.id &&
           source == rhs.source &&
           target == rhs.target &&
           metadata == rhs.metadata;
}

bool Route::operator!=(const Route &rhs) const {
    return !(rhs == *this);
}

void Route::LoadSQLite(const string& sqliteDBPath) {
    SQLite::Database    db(sqliteDBPath);
    const bool tableExists = db.tableExists("Route");
    if (tableExists) {
        SQLite::Statement   query(db, "SELECT sequence_number, connection_id FROM Route;");
        while (query.executeStep())
        {
            const int sequenceNr     = query.getColumn(0);
            const string connectionId  = query.getColumn(1);
            metadata.insert(std::pair<int, string>(sequenceNr, connectionId));
        }
    } else {
        LOG_F(WARNING, "SQLite could not find table 'Route'!");
    }
}