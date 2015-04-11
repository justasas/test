# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20_141_119_083_314) do

  create_table 'accounts', force: true do |t|
    t.string 'name'
    t.string 'pass'
    t.string 'email'
    t.integer 'birth_year'
    t.string 'privilege'
    t.datetime 'created_at'
    t.datetime 'updated_at'
    t.integer 'internet_forum_id'
  end

  add_index 'accounts', ['internet_forum_id'], name: 'index_accounts_on_internet_forum_id'

  create_table 'categories', force: true do |t|
    t.string 'name'
    t.text 'description'
    t.integer 'internet_forum_id'
    t.datetime 'created_at'
    t.datetime 'updated_at'
  end

  add_index 'categories', ['internet_forum_id'], name: 'index_categories_on_internet_forum_id'

  create_table 'forums', force: true do |t|
    t.string 'title'
    t.text 'description'
    t.integer 'account_id'
    t.datetime 'created_at'
    t.datetime 'updated_at'
    t.integer 'category_id'
  end

  add_index 'forums', ['account_id'], name: 'index_forums_on_account_id'
  add_index 'forums', ['category_id'], name: 'index_forums_on_category_id'

  create_table 'internet_forums', force: true do |t|
    t.string 'title'
    t.text 'description'
    t.datetime 'created_at'
    t.datetime 'updated_at'
  end

  create_table 'ratings', force: true do |t|
    t.integer 'topic_id'
    t.integer 'account_id'
    t.integer 'rating'
    t.datetime 'created_at'
    t.datetime 'updated_at'
  end

  add_index 'ratings', ['account_id'], name: 'index_ratings_on_account_id'
  add_index 'ratings', ['topic_id'], name: 'index_ratings_on_topic_id'

  create_table 'replies', force: true do |t|
    t.text 'text'
    t.datetime 'created_at'
    t.datetime 'updated_at'
    t.integer 'account_id'
    t.integer 'topic_id'
    t.integer 'reply_id'
    t.integer 'last_edit_by_id'
  end

  add_index 'replies', ['account_id'], name: 'index_replies_on_account_id'
  add_index 'replies', ['last_edit_by_id'], name: 'index_replies_on_last_edit_by_id'
  add_index 'replies', ['reply_id'], name: 'index_replies_on_reply_id'
  add_index 'replies', ['topic_id'], name: 'index_replies_on_topic_id'

  create_table 'replies_replies', id: false, force: true do |t|
    t.integer 'reply_id'
  end

  add_index 'replies_replies', ['reply_id'], name: 'index_replies_replies_on_reply_id'

  create_table 'topics', force: true do |t|
    t.string 'title'
    t.datetime 'created_at'
    t.datetime 'updated_at'
    t.integer 'account_id'
    t.integer 'forum_id'
  end

  add_index 'topics', ['account_id'], name: 'index_topics_on_account_id'
  add_index 'topics', ['forum_id'], name: 'index_topics_on_forum_id'

end
