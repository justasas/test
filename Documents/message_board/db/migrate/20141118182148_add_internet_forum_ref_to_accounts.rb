class AddInternetForumRefToAccounts < ActiveRecord::Migration
  def change
    add_column :accounts, :internet_forum_id, :integer
  end
end
