# Account
class Account < ActiveRecord::Base
  has_many :replies
  has_many :topics
  has_many :ratings
  has_many :topics, through: :ratings
  belongs_to :internet_forum

  validates :name, presence: true, length: { maximum: 50 },
                   uniqueness: true
  VALID_EMAIL_REGEX = /\A[a-zA-Z0-9_.+\-]+@[a-zA-Z0-9\-]+\.[a-zA-Z0-9\-.]+\z/i
  validates :email, presence: true, length: { maximum: 255 },
                    format: { with: VALID_EMAIL_REGEX },
                    uniqueness: { case_sensitive: false }
  validates :pass, presence: true, length: { minimum: 6 }
end
