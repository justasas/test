# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)

@account = Account.create name: 'vardas', pass: 'slaptazodis',
                          email: 'email@email.com', birth_year: 1993
@reply = Reply.create text: 'text', account: @account
FactoryGirl.create(:topic, 'replies' => [@reply])
FactoryGirl.create(:internet_forum, 'accounts' => [@account])
